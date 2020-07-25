package com.example.eric.newtraveler.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.eric.newtraveler.extension.fromJsonOrElse
import com.example.eric.newtraveler.extension.getStringOrElse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesHelper private constructor() : PreferencesHelper {

    private lateinit var preferences: SharedPreferences

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    override fun getCountyList(): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        val json = preferences.getStringOrElse(TAG_COUNTY_LIST, "")
        return Gson().fromJsonOrElse(json, type) { listOf() }
    }

    override fun updateCountyList(countyList: List<String>) {
        preferences.edit { putString(TAG_COUNTY_LIST, toJson(countyList)) }
    }

    override fun getCityList(countyName: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        val json = preferences.getStringOrElse(TAG_CITY_LIST + "_" + countyName, "")
        return Gson().fromJsonOrElse(json, type) { listOf() }
    }

    override fun updateCityList(
            cityList: List<String>,
            countyName: String
    ) {
        preferences.edit { putString(TAG_CITY_LIST + "_" + countyName, toJson(cityList)) }
    }

    override fun getRemoteConfigResult(): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        val json = preferences.getStringOrElse(TAG_REMOTE_CONFIG, "")
        return Gson().fromJsonOrElse(json, type) { listOf() }
    }

    override fun updateRemoteConfigResult(remoteConfigs: List<String>) {
        preferences.edit { putString(TAG_REMOTE_CONFIG, toJson(remoteConfigs)) }
    }

    private fun <T> toJson(data: T): String {
        val type = object : TypeToken<T>() {}.type
        return Gson().toJson(data, type)
    }

    companion object {
        private const val PREF_FILE_NAME = "travel_pref_file"

        private const val TAG_COUNTY_LIST = "countylist"
        private const val TAG_CITY_LIST = "citylist"
        private const val TAG_REMOTE_CONFIG = "remote_config"

        private val sharedPreferencesHelper = SharedPreferencesHelper()

        fun sharedInstance(): SharedPreferencesHelper = sharedPreferencesHelper
    }
}