package com.example.eric.newtraveler.network

import com.example.eric.newtraveler.network.api.Api
import com.example.eric.newtraveler.network.response.LocationInfo
import com.example.eric.newtraveler.network.response.AttractionDetail
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

    override fun getNormalSearchSpotDetail(spotName: String): Observable<List<AttractionDetail>> {
        return api.getNormalSearchSpotDetail(spotName)
    }

    override fun getKeywordSearchSpotDetail(spotName: String): Observable<List<AttractionDetail>> {
        return api.getKeywordSearchSpotDetail(spotName)
    }

    companion object {
        private val api = RetrofitApi()

        private val instance = NetworkApi()

        @JvmStatic
        fun sharedInstance(): NetworkApi = instance
    }
}