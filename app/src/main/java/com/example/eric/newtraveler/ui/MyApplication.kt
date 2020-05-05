package com.example.eric.newtraveler.ui

import android.app.Application
import android.util.Log
import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.NetworkWeatherApi
import com.squareup.leakcanary.LeakCanary

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.v(MainActivity.TAG, "MyApplication, onCreate ")
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        if (DEBUG) {
            LeakCanary.install(this)
        }
        NetworkApi.sharedInstance().initialize()
        NetworkWeatherApi.sharedInstance().initialize()
    }

    companion object {
        private const val DEBUG = false
    }
}