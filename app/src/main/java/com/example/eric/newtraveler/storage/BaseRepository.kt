package com.example.eric.newtraveler.storage

import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.storage.dao.AttractionDetailDao
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseRepository : KoinComponent {

    protected val attractionDetailDao: AttractionDetailDao by inject()

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

    fun getAllAttractions(): Observable<List<AttractionDetail>> {
        return attractionDetailDao.getAllAttractions()
    }
}