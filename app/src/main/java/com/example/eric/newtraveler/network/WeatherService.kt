package com.example.eric.newtraveler.network

import com.example.eric.newtraveler.network.response.Weather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherService {
    @GET("F-C0032-001")
    fun getWeather(
            @Query("locationName") locationName: String,
            @Header("Authorization") authHeader: String
    ): Observable<Weather>
}