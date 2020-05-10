package com.example.eric.newtraveler.network

import com.example.eric.newtraveler.network.api.WeatherApi
import com.example.eric.newtraveler.network.response.WeatherInfo
import com.example.eric.newtraveler.network.retrofit.RetrofitWeatherApi
import io.reactivex.Observable

class NetworkWeatherApi : WeatherApi {

    override fun getWeather(locationName: String, authHeader: String): Observable<WeatherInfo> {
        return api.getWeather(locationName, authHeader)
    }

    companion object {
        private val api = RetrofitWeatherApi()

        private val instance = NetworkWeatherApi()

        @JvmStatic
        fun sharedInstance(): NetworkWeatherApi = instance
    }
}