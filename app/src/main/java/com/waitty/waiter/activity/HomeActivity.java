package com.waitty.waiter.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waitty.waiter.R;
import com.waitty.waiter.adapter.PagerAdapterHome;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.ActivityHomeBinding;
import com.waitty.waiter.fcm.RegisteFCMId;
import com.waitty.waiter.model.LoginUser;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.utility.Utility;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseActivity {
    private Context mContext;
    private ActivityHomeBinding activityHomeBinding;
    private boolean doubleBackToExitPressedOnce = false;
    private MenuItem searchItem;
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
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        activityHomeBinding= DataBindingUtil.setContentView(this, R.layout.activity_home);
        mContext = this;
        init();
    }

    // Variable initialization
    private void init() {
        try {
            Utility.setSharedPreferencesBoolean(this, constant.IS_LOGIN, true);
            Utility.setSharedPreferencesBoolean(mContext,constant.NEW_RELOAD_BY_FCM,false);
            Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_RELOAD_BY_FCM,false);
            setToolbar();
            setupBottomNavigationView();

            Type type = new TypeToken<LoginUser>() {
            }.getType();
            userInformation = new Gson().fromJson(Utility.getSharedPreferencesString(mContext, constant.USER_INFORMATION), type);

            Utility.CheckApplicationVersion(mContext);

            setNotificationCount();

            if(getIntent().hasExtra(API.DATA)){

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        try{
                            if(getIntent().getExtras().getString(API.DATA).trim().length()>0){
                                    JSONObject NotiOBJ=new JSONObject(getIntent().getExtras().getString(API.DATA));

                                    if(NotiOBJ.getInt(API.ORDER_STATUS_ID)== constant.ORDER_PLACED ){
                                        Utility.setSharedPreferencesInteger(mContext,constant.NOTIFICATION_COUNT_NEW,0);
                                        pagerAdapterHome.statusHomeFragment.newBadgeAction(false);
                                        setOrderHistory(0);
                                    }else if(NotiOBJ.getInt(API.ORDER_STATUS_ID)== constant.ORDER_PREPARING ||
                                            NotiOBJ.getInt(API.ORDER_STATUS_ID)== constant.ORDER_READY_SERVE){
                                        Utility.setSharedPreferencesInteger(mContext,constant.NOTIFICATION_COUNT_PROCESSING,0);
                                        pagerAdapterHome.statusHomeFragment.processingBadgeAction(false);
                                        setOrderHistory(1);
                                    }else
                                        setOrderHistory(2);
                            }
                        }catch (Exception e){e.printStackTrace();}
                    }
                }, 200);

            }else
                Utility.setSharedPreferencesInteger(mContext,constant.NOTIFICATION_COUNT_NEW,0);

        }catch (Exception e){e.printStackTrace();}
    }

    // Change order history tab position
    public void setOrderHistory(int tabPosition) {
        pagerAdapterHome.statusHomeFragment.chageTab(tabPosition);
    }

    // Set notification badge on top tab
    private void setNotificationCount() {

        timer=new Timer();
        handler = new Handler();

        update = new Runnable() {
            public void run() {
                int totalUnreadNewCount = Utility.getSharedPreferencesInteger(mContext,constant.NOTIFICATION_COUNT_NEW);
                if(totalUnreadNewCount>0)
                    pagerAdapterHome.statusHomeFragment.newBadgeAction(true);
                else
                    pagerAdapterHome.statusHomeFragment.newBadgeAction(false);

                int totalUnreadProcessingCount = Utility.getSharedPreferencesInteger(mContext,constant.NOTIFICATION_COUNT_PROCESSING);
                if(totalUnreadProcessingCount>0)
                    pagerAdapterHome.statusHomeFragment.processingBadgeAction(true);
                else
                    pagerAdapterHome.statusHomeFragment.processingBadgeAction(false);

                if(Utility.getSharedPreferencesBoolean(mContext,constant.NEW_RELOAD_BY_FCM)){
                    Utility.setSharedPreferencesBoolean(mContext,constant.NEW_RELOAD_BY_FCM,false);
                    if(Utility.isNetworkAvailable(mContext))
                        pagerAdapterHome.statusHomeFragment.adapter.newOrderListFragment.refreshList(false);
                }

                if(Utility.getSharedPreferencesBoolean(mContext,constant.PROCESSING_RELOAD_BY_FCM)){
                    Utility.setSharedPreferencesBoolean(mContext,constant.PROCESSING_RELOAD_BY_FCM,false);
                    if(Utility.isNetworkAvailable(mContext))
                        pagerAdapterHome.statusHomeFragment.adapter.processingOrderListFragment.refreshList(false);
                }
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 5000, 5000);

    }

    // Set toolbar
    private void setToolbar() {
        setSupportActionBar(activityHomeBinding.toolbarActionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    // Set bottom navigation tab view
    private void setupBottomNavigationView() {
        try{
            pagerAdapterHome = new PagerAdapterHome(getSupportFragmentManager(), mContext);
            activityHomeBinding.pager.setAdapter(pagerAdapterHome);
            activityHomeBinding.pager.setCurrentItem(0);
            activityHomeBinding.pager.setOffscreenPageLimit(3);
            pagerAdapterHome.notifyDataSetChanged();

            activityHomeBinding.BottomNavigation.setItemIconTintList(null);

            activityHomeBinding.pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    selectFragment(activityHomeBinding.BottomNavigation.getMenu().getItem(position));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            activityHomeBinding.BottomNavigation.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });

        }catch (Exception e){e.printStackTrace();}
    }

    // Set fragment for tab
    protected void selectFragment(MenuItem item) {
        searchItem.setVisible(false);
        item.setChecked(true);

        activityHomeBinding.BottomNavigation.getMenu().findItem(R.id.statusHome).setIcon(R.drawable.status_bottom);
        activityHomeBinding.BottomNavigation.getMenu().findItem(R.id.menuHome).setIcon(R.drawable.menu_bottom);
        activityHomeBinding.BottomNavigation.getMenu().findItem(R.id.profileHome).setIcon(R.drawable.profile_bottom);

        switch (item.getItemId()) {
            case R.id.statusHome:
                activityHomeBinding.pager.setCurrentItem(0, false);
                pagerAdapterHome.notifyDataSetChanged();
                activityHomeBinding.txtHeading.setText(R.string.txt_order_status);
                activityHomeBinding.txtHeading.setPaddingRelative((int) getResources().getDimension(R.dimen._28sdp),0,0,0);
                item.setIcon(R.drawable.status_selected_bottom);
                searchItem.setVisible(true);
                break;
            case R.id.menuHome:
                activityHomeBinding.pager.setCurrentItem(1, false);
                pagerAdapterHome.notifyDataSetChanged();
                activityHomeBinding.txtHeading.setText(userInformation.getRestaurant().get(0).getName());
                activityHomeBinding.txtHeading.setPaddingRelative(0,0,(int) getResources().getDimension(R.dimen._15sdp),0);
                item.setIcon(R.drawable.menu_selected_bottom);
                break;
            case R.id.profileHome:
                activityHomeBinding.pager.setCurrentItem(2, false);
                pagerAdapterHome.notifyDataSetChanged();
                activityHomeBinding.txtHeading.setText(R.string.txt_profile);
                activityHomeBinding.txtHeading.setPaddingRelative(0,0,(int) getResources().getDimension(R.dimen._15sdp),0);
                item.setIcon(R.drawable.profile_selected_bottom);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_search, menu);
        searchItem=menu.findItem(R.id.itemSearch);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemSearch) {
            startActivity(new Intent(mContext, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        if (doubleBackToExitPressedOnce) {
            finish();
            System.exit(0);
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utility.ShowSnackbar(mContext,activityHomeBinding.Cordinator,getString(R.string.back_msg));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
