package com.example.eric.newtraveler.network

import com.example.eric.newtraveler.network.api.Api
import com.example.eric.newtraveler.network.response.SpotDetail
import com.example.eric.newtraveler.network.response.TravelCountyAndCity
import com.example.eric.newtraveler.network.retrofit.RetrofitApi
import io.reactivex.Observable
import java.util.*

class NetworkApi : Api {

    override fun getAllCountyAndCityList(): Observable<ArrayList<TravelCountyAndCity>> {
        return api.getAllCountyAndCityList()
    }

    override fun getAllCountyList(): Observable<ArrayList<String>> {
        return api.getAllCountyList()
    }

    override fun getCityList(county: String): Observable<ArrayList<String>> {
        return api.getCityList(county)
    }

    override fun getNormalSearchSpotDetail(spotName: String): Observable<ArrayList<SpotDetail>> {
        return api.getNormalSearchSpotDetail(spotName)
    }

    override fun getKeywordSearchSpotDetail(spotName: String): Observable<ArrayList<SpotDetail>> {
        return api.getKeywordSearchSpotDetail(spotName)
    }

    companion object {
        private val api = RetrofitApi()

        private val instance = NetworkApi()

        @JvmStatic
        fun sharedInstance(): NetworkApi = instance
    }
}