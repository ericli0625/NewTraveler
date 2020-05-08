package com.example.eric.newtraveler.storage

import org.koin.core.KoinComponent

abstract class BaseRepository : KoinComponent {

    protected val preferencesHelper: PreferencesHelper by lazy { SharedPreferencesHelper.sharedInstance() }

    fun getCountyList(): List<String> {
        return preferencesHelper.getCountyList()
    }

    fun getCityList(countyName: String): List<String> {
        return preferencesHelper.getCityList(countyName)
    }
}