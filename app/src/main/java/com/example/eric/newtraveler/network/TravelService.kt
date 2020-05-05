package com.example.eric.newtraveler.network

import com.example.eric.newtraveler.network.response.SpotDetail
import com.example.eric.newtraveler.network.response.TravelCountyAndCity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface TravelService {
    @GET("travelcity")
    fun getAllCountyAndCityList(): Observable<ArrayList<TravelCountyAndCity>>

    @GET("travelcity/all_county/")
    fun getAllCountyList(): Observable<ArrayList<String>>

    @GET("travelcity/query_city")
    fun getCityList(
            @Query("county") county: String
    ): Observable<ArrayList<String>>

    @GET("travelspot/query_spot")
    fun getNormalSearchSpotDetail(
            @Query("place") spotName: String
    ): Observable<ArrayList<SpotDetail>>

    @GET("travelspot/query_spot_name")
    fun getKeywordSearchSpotDetail(
            @Query("spot_name") spotName: String
    ): Observable<ArrayList<SpotDetail>>
}