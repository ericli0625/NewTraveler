package com.example.eric.newtraveler.network.service

import com.example.eric.newtraveler.network.response.LocationInfo
import com.example.eric.newtraveler.network.response.AttractionDetail
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TravelService {
    @GET("travelcity")
    fun getLocationList(): Observable<List<LocationInfo>>

    @GET("travelcity/all_county/")
    fun getCountyList(): Observable<List<String>>

    @GET("travelcity/query_city")
    fun getCityList(
            @Query("county") county: String
    ): Observable<List<String>>

    @GET("travelspot/query_spot")
    fun getNormalSearchSpotDetail(
            @Query("place") spotName: String
    ): Observable<List<AttractionDetail>>

    @GET("travelspot/query_spot_name")
    fun getKeywordSearchSpotDetail(
            @Query("spot_name") spotName: String
    ): Observable<List<AttractionDetail>>
}