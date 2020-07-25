package com.example.eric.newtraveler.storage

import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.storage.dao.AttractionInfoDao
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseRepository : KoinComponent {

    protected val attractionInfoDao: AttractionInfoDao by inject()

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

    fun getRemoteConfigResult(): List<String> {
        return preferencesHelper.getRemoteConfigResult()
    }

    fun getAllAttractions(): Observable<List<AttractionInfo>> {
        return attractionInfoDao.getAllAttractions()
    }
}