package com.example.eric.newtraveler.network.api

import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.network.response.LocationInfo
import io.reactivex.Observable

interface Api {

    fun getLocationList(): Observable<List<LocationInfo>>

    fun getCountyList(): Observable<List<String>>

    fun getCityList(county: String): Observable<List<String>>

    fun getAttractionInfoByPlace(placeName: String): Observable<List<AttractionInfo>>

    fun getAttractionInfoByName(attractionName: String): Observable<List<AttractionInfo>>
}