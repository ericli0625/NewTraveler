package com.example.eric.newtraveler.ui;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends Application {

    private static final boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(MainActivity.TAG, "MyApplication, onCreate ");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        if (DEBUG) {
            LeakCanary.install(this);
        }
    }

}
