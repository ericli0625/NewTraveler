package com.example.eric.newtraveler.network

import okhttp3.logging.HttpLoggingInterceptor

object Config {
    const val BASE_API_URL = "https://travelplanbackend.herokuapp.com/api/"
    const val BASE_WEATHER_API_URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/"
    val INTERCEPTOR_LEVEL = HttpLoggingInterceptor.Level.BODY
}