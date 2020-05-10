package com.example.eric.newtraveler.network.api

import com.example.eric.newtraveler.network.response.SpotDetail
import com.example.eric.newtraveler.network.response.TravelCountyAndCity
import io.reactivex.Observable
import java.util.*

interface Api {

    fun getAllCountyAndCityList(): Observable<ArrayList<TravelCountyAndCity>>

    fun getAllCountyList(): Observable<ArrayList<String>>

    fun getCityList(county: String): Observable<ArrayList<String>>

    fun getNormalSearchSpotDetail(spotName: String): Observable<ArrayList<SpotDetail>>

    fun getKeywordSearchSpotDetail(spotName: String): Observable<ArrayList<SpotDetail>>
}