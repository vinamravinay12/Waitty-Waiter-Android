package com.waitty.waiter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager;
import com.waitty.kitchen.fragment.FragmentUtils;
import com.waitty.waiter.R;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.FragmentProfileHomeBinding;
import com.waitty.waiter.model.LoginUser;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.utility.Dialog;
import com.waitty.waiter.utility.Utility;

import java.lang.reflect.Type;

public class ProfileHomeFragment extends Fragment  {

    private ViewGroup root;
    private Context mContext;
    FragmentProfileHomeBinding fragmentProfileHomeBinding;
    private ClickHandler mclickHandler;
    private LoginUser userInformation;
    private BottomSheetBehavior bottomSheetBehavior;

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
        bottomSheetBehavior = BottomSheetBehavior.from(fragmentProfileHomeBinding.profileHome);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setPeekHeight(500,true);

        fragmentProfileHomeBinding.btnLogout.setOnClickListener(v ->  mclickHandler.logoutClick(v));
        fragmentProfileHomeBinding.setClickEvent(mclickHandler);

      bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
          @Override
          public void onStateChanged(@NonNull View bottomSheet, int newState) {
              if(newState == BottomSheetBehavior.STATE_COLLAPSED) {
                  closeProfileFragment();
              }
          }

          @Override
          public void onSlide(@NonNull View bottomSheet, float slideOffset) {

          }
      });
        setUserData();
    }

    private void closeProfileFragment() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(constant.TAG_HOME);
        FragmentUtils.INSTANCE.launchFragment(getActivity().getSupportFragmentManager(),R.id.nav_host_fragment,fragment,constant.TAG_HOME);
    }

    // Set user information
    private void setUserData() {
        Type type = new TypeToken<LoginUser>() { }.getType();
        userInformation = new Gson().fromJson(new SharedPreferenceManager(getContext(), constant.LOGIN_SP).getStringPreference(API.WAITER_USER), type);

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

//        try{
//            JsonObject jsonObject=new JsonObject();
//            jsonObject.addProperty(API.NOTIFICATION_TOGGLE, isChecked);
//
//            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            Call<JsonElement> call = apiInterface.profileUpdate(jsonObject,Utility.getSharedPreferencesString(mContext,constant.USER_SECURITY_TOKEN));
//            new APICall(mContext).Server_Interaction(call,this ,API.UPDATE_PROFILE);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }


}