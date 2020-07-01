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
import androidx.lifecycle.Observer;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager;
import com.waitty.kitchen.fragment.FragmentUtils;
import com.waitty.waiter.R;
import com.waitty.waiter.activity.HomeActivity;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.FragmentProfileHomeBinding;
import com.waitty.waiter.model.APIStatus;
import com.waitty.waiter.model.LoginUser;
import com.waitty.waiter.model.WaittyAPIResponse;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.retrofit.ApiClient;
import com.waitty.waiter.utility.Dialog;
import com.waitty.waiter.utility.Utility;
import com.waitty.waiter.viewmodel.repository.NotificationsRepository;

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
        if(getActivity() instanceof HomeActivity) {
            ((HomeActivity)getActivity()).setPageTitle(getString(R.string.txt_title_profile));
        }

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
        bottomSheetBehavior.setPeekHeight(300,true);


        fragmentProfileHomeBinding.btnLogout.setOnClickListener(v ->  mclickHandler.logoutClick(v));
        fragmentProfileHomeBinding.cvPrivacyPolicy.setOnClickListener(v ->  mclickHandler.privacyPolicyClick(v));
        fragmentProfileHomeBinding.cvTerms.setOnClickListener(v ->  mclickHandler.termsConditionsClick(v));
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

                if(slideOffset <= 0.15f){
                  closeProfileFragment();
              }
          }
      });
        setUserData();
    }

    private void closeProfileFragment() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(constant.TAG_HOME);

        FragmentUtils.INSTANCE.launchFragment(getActivity().getSupportFragmentManager(),R.id.nav_host_fragment,fragment != null ? fragment : new HomeFragment(),constant.TAG_HOME);
    }

    // Set user information
    private void setUserData() {
        Type type = new TypeToken<LoginUser>() { }.getType();
        userInformation = new Gson().fromJson(new SharedPreferenceManager(getContext(), constant.LOGIN_SP).getStringPreference(API.WAITER_USER), type);

        fragmentProfileHomeBinding.txtName.setText(userInformation.getName());
        fragmentProfileHomeBinding.txtWaiterID.setText(getString(R.string.text_waiterid)+" "+userInformation.getKey());

        fragmentProfileHomeBinding.switchNotifications.setChecked(new SharedPreferenceManager(getContext(),constant.NOTIFICATION_SP).getBooleanPreference(constant.NOTIFICATIONS_SHOW,false));

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
            Dialog.showLogoutDialog(getActivity());
        }

    }



    // Change notifications settings API
    private void changeNotificationsSettingsAPI(boolean isChecked) {

        NotificationsRepository notificationsRepository = new NotificationsRepository(ApiClient.getAPIInterface());
        JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty(API.NOTIFICATION_TOGGLE, isChecked);
        notificationsRepository.changeNotificationSettings(Utility.getToken(getContext()),jsonObject).observe(getViewLifecycleOwner(), new Observer<WaittyAPIResponse>() {
            @Override
            public void onChanged(WaittyAPIResponse waittyAPIResponse) {
                if(waittyAPIResponse.getStatus() == APIStatus.SUCCESS) {
                    new SharedPreferenceManager(getContext(),constant.NOTIFICATION_SP).storeBooleanPreference(constant.NOTIFICATIONS_SHOW,isChecked);
                }

                else if(waittyAPIResponse.getStatus() == APIStatus.ERROR) {
                    new SharedPreferenceManager(getContext(),constant.NOTIFICATION_SP).storeBooleanPreference(constant.NOTIFICATIONS_SHOW,false);
                }
            }
        });

    }


}