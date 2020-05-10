package com.example.eric.newtraveler.network.retrofit

import com.example.eric.newtraveler.network.Config.BASE_API_URL
import com.example.eric.newtraveler.network.api.Api
import com.example.eric.newtraveler.network.response.SpotDetail
import com.example.eric.newtraveler.network.response.TravelCountyAndCity
import com.example.eric.newtraveler.network.service.TravelService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RetrofitApi : Api {

    private val travelService: TravelService by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
                )
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitClient.getClient())
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