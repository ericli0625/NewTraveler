package com.example.eric.newtraveler.network.api

import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.network.response.LocationInfo
import io.reactivex.Observable

interface Api {

    fun getLocationList(): Observable<List<LocationInfo>>

    fun getCountyList(): Observable<List<String>>

    fun getCityList(county: String): Observable<List<String>>

    fun getNormalSearchSpotDetail(spotName: String): Observable<List<AttractionDetail>>

    fun getKeywordSearchSpotDetail(spotName: String): Observable<List<AttractionDetail>>
}