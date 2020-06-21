package com.waitty.waiter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager;
import com.waitty.waiter.R;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.databinding.ActivitySplashBinding;
import com.waitty.waiter.utility.NotificationHelper;
import com.waitty.waiter.utility.Utility;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding activitySplashBinding;
    private Context mContext;
    private Thread mThread;
    private boolean isFinish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activitySplashBinding= DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mContext = this;
        init();
    }


    private void init() {
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this,constant.IS_CHANNEL_PREPARED);
        if (!sharedPreferenceManager.getBooleanPreference(constant.IS_CHANNEL_PREPARED,false) && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            new NotificationHelper(mContext);

        isFinish = false;
        mThread = new Thread(mRunnable);
        mThread.start();
    }

    // Screen sleep for some time
    private Runnable mRunnable = new Runnable() {

        public void run() {
            SystemClock.sleep(3000);
            mHandler.sendEmptyMessage(0);
        }
    };

    // Screen sleep out for given time
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0 && (!isFinish)) {
                isFinish = true;
                stop();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        try {
            mThread.interrupt();
            mThread = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    // Next screen move functionality
    private void stop() {
        if (isFinish) {

          if(!alreadyLoggedIn()) launchLoginActivity();
          else launchHomeActivity();
        }
    }

    private void launchLoginActivity() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private Boolean alreadyLoggedIn(){
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this,constant.LOGIN_SP);
        if(sharedPreferenceManager.getBooleanPreference(constant.KEY_IS_LOGGED_IN,false)) {
            return true;
        }
        return false;
    }

    private void launchHomeActivity() {
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFinish)
                stop();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        isFinish = false;
        System.exit(0);
        finish();
        super.onBackPressed();
    }
}
