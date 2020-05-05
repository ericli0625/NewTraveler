package com.example.eric.newtraveler.network

import com.example.eric.newtraveler.network.response.Weather
import com.example.eric.newtraveler.network.retrofit.RetrofitWeatherApi
import io.reactivex.Observable

class NetworkWeatherApi : WeatherApi {

    override fun initialize() {
        return api.initialize()
    }

    override fun getWeather(locationName: String, authHeader: String): Observable<Weather> {
        return api.getWeather(locationName, authHeader)
    }

    companion object {
        private val api = RetrofitWeatherApi()

        private val instance = NetworkWeatherApi()

        @JvmStatic
        fun sharedInstance(): NetworkWeatherApi = instance
    }
}