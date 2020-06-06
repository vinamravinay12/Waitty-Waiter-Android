package com.waitty.waiter.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.waitty.waiter.R;
import com.waitty.waiter.appinterface.getResponseData;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.FragmentProfileHomeBinding;
import com.waitty.waiter.model.LoginUser;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.retrofit.APICall;
import com.waitty.waiter.retrofit.ApiClient;
import com.waitty.waiter.retrofit.ApiInterface;
import com.waitty.waiter.utility.Dialog;
import com.waitty.waiter.utility.Utility;
import org.json.JSONObject;
import java.lang.reflect.Type;
import retrofit2.Call;

public class ProfileHomeFragment extends Fragment implements getResponseData {

    private ViewGroup root;
    private Context mContext;
    FragmentProfileHomeBinding fragmentProfileHomeBinding;
    private ClickHandler mclickHandler;
    private LoginUser userInformation;

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentProfileHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_home, container, false);
        root = (ViewGroup) fragmentProfileHomeBinding.getRoot();
        mContext = getContext();
        init();
        return root;
    }

    // Variable initialization
    private void init() {
        mclickHandler = new ClickHandler();
        fragmentProfileHomeBinding.setClickEvent(mclickHandler);
        setUserData();
    }

    // Set user information
    private void setUserData() {
        Type type = new TypeToken<LoginUser>() { }.getType();
        userInformation = new Gson().fromJson(Utility.getSharedPreferencesString(mContext, constant.USER_INFORMATION), type);

        fragmentProfileHomeBinding.txtName.setText(userInformation.getName());
        fragmentProfileHomeBinding.txtWaiterID.setText(getString(R.string.text_waiterid)+" "+userInformation.getKey());

        fragmentProfileHomeBinding.switchNotifications.setChecked(Utility.getSharedPreferencesBoolean(mContext, constant.NOTIFICATIONS_SHOW));

        //switch change for notification
        fragmentProfileHomeBinding.switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Utility.isNetworkAvailable(mContext))
                    changeNotificationsSettingsAPI(isChecked);
                else{
                    fragmentProfileHomeBinding.switchNotifications.setChecked(!isChecked);
                    Utility.ShowSnackbar(mContext, fragmentProfileHomeBinding.Cordinator, getString(R.string.check_network));
                }
            }
        });
    }

    // Click event handler
    public class ClickHandler {

        public ClickHandler( ) {
        }

        public void privacyPolicyClick(View view) {
            if(Utility.isNetworkAvailable(mContext))
                Utility.openURL(mContext, constant.PRIVACY_POLICY_URL);
            else
                Utility.ShowSnackbar(mContext, fragmentProfileHomeBinding.Cordinator, getString(R.string.check_network));
        }

        public void termsConditionsClick(View view) {
            if(Utility.isNetworkAvailable(mContext))
                Utility.openURL(mContext, constant.TERMS_CONDITIONS_URL);
            else
                Utility.ShowSnackbar(mContext, fragmentProfileHomeBinding.Cordinator, getString(R.string.check_network));
        }

        public void logoutClick(View view) {
            Dialog.showLogoutDialog(mContext);
        }

    }

    // Change notifications settings API
    private void changeNotificationsSettingsAPI(boolean isChecked) {

        try{
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.NOTIFICATION_TOGGLE, isChecked);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.profileUpdate(jsonObject,Utility.getSharedPreferencesString(mContext,constant.USER_SECURITY_TOKEN));
            new APICall(mContext).Server_Interaction(call,this ,API.UPDATE_PROFILE);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject OBJ, String msg, String typeAPI) {

        try {
            if(OBJ.length()>0 && OBJ!=null){
                switch (typeAPI) {
                    case API.UPDATE_PROFILE:
                        Utility.setSharedPreferencesBoolean(mContext, constant.NOTIFICATIONS_SHOW, fragmentProfileHomeBinding.switchNotifications.isChecked());
                        break;
                }
            }
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onFailed(String msg, String typeAPI) {
        if(!msg.isEmpty()){
            switch (typeAPI) {
                case API.UPDATE_PROFILE:
                    fragmentProfileHomeBinding.switchNotifications.setChecked(!fragmentProfileHomeBinding.switchNotifications.isChecked());
                    Utility.ShowSnackbar(mContext, fragmentProfileHomeBinding.Cordinator,msg);
                    break;
            }
        }
    }
}