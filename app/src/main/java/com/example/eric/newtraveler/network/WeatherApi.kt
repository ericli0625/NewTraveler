package com.example.eric.newtraveler.network

import com.example.eric.newtraveler.network.response.Weather
import io.reactivex.Observable

interface WeatherApi {

    fun initialize()

    fun getWeather(locationName: String, authHeader: String): Observable<Weather>
}