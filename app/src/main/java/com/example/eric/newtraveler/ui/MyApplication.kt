package com.example.eric.newtraveler.ui

import android.app.Application
import com.example.eric.newtraveler.koin.appModule
import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.NetworkWeatherApi
import com.example.eric.newtraveler.storage.SharedPreferencesHelper
import com.squareup.leakcanary.LeakCanary
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        if (DEBUG) {
            LeakCanary.install(this)
        }
        SharedPreferencesHelper.sharedInstance().apply { initialize(this@MyApplication) }
    }

    companion object {
        private const val DEBUG = false
    }
}