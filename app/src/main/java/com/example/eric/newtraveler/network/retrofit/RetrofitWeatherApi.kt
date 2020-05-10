package com.example.eric.newtraveler.network.retrofit

import com.example.eric.newtraveler.network.Config.BASE_WEATHER_API_URL
import com.example.eric.newtraveler.network.api.WeatherApi
import com.example.eric.newtraveler.network.response.WeatherInfo
import com.example.eric.newtraveler.network.service.WeatherService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitWeatherApi : WeatherApi {

    private val weatherService: WeatherService by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_WEATHER_API_URL)
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
                )
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitClient.getClient())
                .build()
                .create(WeatherService::class.java)
    }

    override fun getWeather(locationName: String, authHeader: String): Observable<WeatherInfo> {
        return weatherService.getWeather(locationName, authHeader)
    }
}