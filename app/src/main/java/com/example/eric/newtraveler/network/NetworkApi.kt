package com.example.eric.newtraveler.network

import com.example.eric.newtraveler.network.api.Api
import com.example.eric.newtraveler.network.response.LocationInfo
import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.network.retrofit.RetrofitApi
import io.reactivex.Observable

class NetworkApi : Api {

    override fun getLocationList(): Observable<List<LocationInfo>> {
        return api.getLocationList()
    }

    override fun getCountyList(): Observable<List<String>> {
        return api.getCountyList()
    }

    override fun getCityList(county: String): Observable<List<String>> {
        return api.getCityList(county)
    }

    override fun getAttractionInfoByPlace(placeName: String): Observable<List<AttractionInfo>> {
        return api.getAttractionInfoByPlace(placeName)
    }

    override fun getAttractionInfoByName(attractionName: String): Observable<List<AttractionInfo>> {
        return api.getAttractionInfoByName(attractionName)
    }

    companion object {
        private val api = RetrofitApi()

        private val instance = NetworkApi()

        @JvmStatic
        fun sharedInstance(): NetworkApi = instance
    }
}