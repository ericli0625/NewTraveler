package com.example.eric.newtraveler.network.api

import com.example.eric.newtraveler.network.response.WeatherInfo
import io.reactivex.Observable

interface WeatherApi {

    fun getWeather(locationName: String, authHeader: String): Observable<WeatherInfo>
}