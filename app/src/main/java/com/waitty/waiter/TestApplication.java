package com.waitty.waiter;

import android.app.Application;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

       /* try {
            Mint.setApplicationEnvironment(Mint.appEnvironmentTesting);
            Mint.initAndStartSession(this, "51868c11");
        } catch (Exception e) {e.printStackTrace();}*/

    }

}

