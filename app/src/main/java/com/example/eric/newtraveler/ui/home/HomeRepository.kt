package com.example.eric.newtraveler.ui.home

import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.response.TravelCountyAndCity
import com.example.eric.newtraveler.storage.BaseRepository
import io.reactivex.Observable
import java.util.*

class HomeRepository : BaseRepository() {

    fun queryAllCountyAndCityList(): Observable<List<String>> {
        return NetworkApi.sharedInstance().getAllCountyAndCityList()
                .map(::parserAllCountyAndCityList)
    }

    private fun parserAllCountyAndCityList(
            arrayList: ArrayList<TravelCountyAndCity>
    ): List<String> {
        val countyList = arrayList
                .map(TravelCountyAndCity::getCounty)
                .distinct()
        preferencesHelper.updateCountyList(countyList)

        countyList.forEach { county ->
            val cityList = arrayList
                    .filter { county == it.county }
                    .map { it.city }
            preferencesHelper.updateCityList(cityList, county)
        }
        return countyList
    }
}