package com.example.eric.newtraveler.network.retrofit

import com.example.eric.newtraveler.network.Api
import com.example.eric.newtraveler.network.TravelService
import com.example.eric.newtraveler.network.response.SpotDetail
import com.example.eric.newtraveler.network.response.TravelCountyAndCity
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RetrofitApi : Api {

    private lateinit var travelService: TravelService

    override fun initialize() {
        travelService = Retrofit.Builder()
                .baseUrl("https://travelplanbackend.herokuapp.com/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TravelService::class.java)
    }

    override fun getAllCountyAndCityList(): Observable<ArrayList<TravelCountyAndCity>> {
        return travelService.getAllCountyAndCityList()
    }

    override fun getAllCountyList(): Observable<ArrayList<String>> {
        return travelService.getAllCountyList()
    }

    override fun getCityList(county: String): Observable<ArrayList<String>> {
        return travelService.getCityList(county)
    }

    override fun getNormalSearchSpotDetail(spotName: String): Observable<ArrayList<SpotDetail>> {
        return travelService.getNormalSearchSpotDetail(spotName)
    }

    override fun getKeywordSearchSpotDetail(spotName: String): Observable<ArrayList<SpotDetail>> {
        return travelService.getKeywordSearchSpotDetail(spotName)
    }
}