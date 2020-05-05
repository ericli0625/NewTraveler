package com.example.eric.newtraveler.network.retrofit

import com.example.eric.newtraveler.network.WeatherApi
import com.example.eric.newtraveler.network.WeatherService
import com.example.eric.newtraveler.network.response.Weather
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitWeatherApi : WeatherApi {

    private lateinit var weatherService: WeatherService

    override fun initialize() {
        weatherService = Retrofit.Builder()
                .baseUrl("https://opendata.cwb.gov.tw/api/v1/rest/datastore/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherService::class.java)
    }

    override fun getWeather(locationName: String, authHeader: String): Observable<Weather> {
        return weatherService.getWeather(locationName, authHeader)
    }
}