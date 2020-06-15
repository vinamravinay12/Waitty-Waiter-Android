package com.waitty.waiter.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.waitty.kitchen.fragment.FragmentUtils;
import com.waitty.waiter.R;
import com.waitty.waiter.adapter.PagerAdapterHome;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.ActivityHomeBinding;
import com.waitty.waiter.fragment.HomeFragment;
import com.waitty.waiter.fragment.MenuFragment;
import com.waitty.waiter.fragment.ProfileHomeFragment;
import com.waitty.waiter.fragment.SettingsFragment;
import com.waitty.waiter.model.LoginUser;

import java.util.Timer;

public class HomeActivity extends AppCompatActivity {
    private Context mContext;
    private boolean doubleBackToExitPressedOnce = false;
    private MenuItem searchItem;
    private ActivityHomeBinding activityHomeBinding;
    public PagerAdapterHome pagerAdapterHome;
    public LoginUser userInformation;
    private Handler handler;
    private Runnable update;
    private Timer timer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mContext = this;
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activityHomeBinding.ibBtnBack.setOnClickListener(v -> { handleBackButtonClick();});

        activityHomeBinding.navView.setOnNavigationItemSelectedListener(item -> launchSelectedFragment(item));
    }

    private boolean launchSelectedFragment(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home :
                launchHomeFragment();
                return true;

            case R.id.navigation_menu :
                FragmentUtils.INSTANCE.launchFragment(getSupportFragmentManager(), R.id.nav_host_fragment,new MenuFragment(), constant.TAG_MENU);
                return true;

            case R.id.navigation_profile :
                FragmentUtils.INSTANCE.launchFragment(getSupportFragmentManager(),R.id.nav_host_fragment,new ProfileHomeFragment(),constant.TAG_PROFILE);
                return true;

            case R.id.navigation_settings :
                FragmentUtils.INSTANCE.launchFragment(getSupportFragmentManager(),R.id.nav_host_fragment,new SettingsFragment(),constant.TAG_SETTINGS);
                return true;
        }
        return false;
    }

    private void launchHomeFragment() {
        FragmentUtils.INSTANCE.launchFragment(getSupportFragmentManager(), R.id.nav_host_fragment,new HomeFragment(), constant.TAG_HOME);
    }

    private void handleBackButtonClick() {
        FragmentUtils.INSTANCE.goBackToPreviousScreen(this,null,null);
    }

    public void setBackButtonVisibility(boolean toShow) {
        activityHomeBinding.ibBtnBack.setVisibility(toShow ? View.VISIBLE : View.GONE);
    }

    public void setPageTitle(String title) {
        activityHomeBinding.tvPageTitle.setText(title);
    }


}
