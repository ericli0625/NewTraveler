package com.example.eric.newtraveler.ui.home

import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.response.LocationInfo
import com.example.eric.newtraveler.storage.BaseRepository
import io.reactivex.Observable

class HomeRepository : BaseRepository() {

    fun queryLocationList(): Observable<List<String>> {
        return NetworkApi.sharedInstance().getLocationList()
                .map(::parserLocationList)
    }

    private fun parserLocationList(list: List<LocationInfo>): List<String> {
        val countyList = list
                .map(LocationInfo::county)
                .distinct()
        preferencesHelper.updateCountyList(countyList)

        countyList.forEach { county ->
            val cityList = list
                    .filter { county == it.county }
                    .map { it.city }
            preferencesHelper.updateCityList(cityList, county)
        }
        return countyList
    }
}