package com.example.eric.newtraveler.network.service

import com.example.eric.newtraveler.network.response.LocationInfo
import com.example.eric.newtraveler.network.response.AttractionInfo
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
    fun getAttractionInfoByPlace(
            @Query("place") placeName: String
    ): Observable<List<AttractionInfo>>

    @GET("travelspot/query_spot_name")
    fun getAttractionInfoByName(
            @Query("spot_name") attractionName: String
    ): Observable<List<AttractionInfo>>
}