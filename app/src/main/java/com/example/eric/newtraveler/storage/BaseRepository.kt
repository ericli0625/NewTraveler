package com.example.eric.newtraveler.storage

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.koin.core.KoinComponent

abstract class BaseRepository : KoinComponent {

    protected val remoteConfig by lazy { Firebase.remoteConfig }
    protected val preferencesHelper: PreferencesHelper by lazy { SharedPreferencesHelper.sharedInstance() }

    fun initRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun getCountyList(): List<String> {
        return preferencesHelper.getCountyList()
    }

    fun getCityList(countyName: String): List<String> {
        return preferencesHelper.getCityList(countyName)
    }
}