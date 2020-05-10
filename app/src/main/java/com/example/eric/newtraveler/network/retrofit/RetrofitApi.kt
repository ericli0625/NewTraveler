package com.example.eric.newtraveler.network.retrofit

import com.example.eric.newtraveler.network.Config.BASE_API_URL
import com.example.eric.newtraveler.network.api.Api
import com.example.eric.newtraveler.network.response.LocationInfo
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.network.service.TravelService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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

    override fun getLocationList(): Observable<List<LocationInfo>> {
        return travelService.getLocationList()
    }

    override fun getCountyList(): Observable<List<String>> {
        return travelService.getCountyList()
    }

    override fun getCityList(county: String): Observable<List<String>> {
        return travelService.getCityList(county)
    }

    override fun getNormalSearchSpotDetail(spotName: String): Observable<List<AttractionDetail>> {
        return travelService.getNormalSearchSpotDetail(spotName)
    }

    override fun getKeywordSearchSpotDetail(spotName: String): Observable<List<AttractionDetail>> {
        return travelService.getKeywordSearchSpotDetail(spotName)
    }
}