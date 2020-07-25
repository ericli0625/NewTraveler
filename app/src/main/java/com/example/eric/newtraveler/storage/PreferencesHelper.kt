package com.example.eric.newtraveler.storage

interface PreferencesHelper {

    fun getCountyList(): List<String>
    fun updateCountyList(countyList: List<String>)

    fun getCityList(countyName: String): List<String>
    fun updateCityList(
            cityList: List<String>,
            countyName: String
    )

    fun getRemoteConfigResult(): List<String>
    fun updateRemoteConfigResult(remoteConfigs: List<String>)
}