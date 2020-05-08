package com.example.eric.newtraveler.storage

import java.util.*

interface PreferencesHelper {

    fun getCountyList(): List<String>
    fun updateCountyList(countyList: List<String>)

    fun getCityList(countyName: String): List<String>
    fun updateCityList(
            cityList: List<String>,
            countyName: String
    )
}