package com.example.eric.newtraveler.storage

import io.reactivex.Observable
import org.koin.core.KoinComponent

abstract class BaseRepository : KoinComponent {

    protected val preferencesHelper: PreferencesHelper by lazy { SharedPreferencesHelper.sharedInstance() }

    fun getCountyList(): Observable<List<String>> {
        return Observable.just(preferencesHelper.getCountyList())
    }

    fun getCityList(countyName: String): Observable<List<String>> {
        return Observable.just(preferencesHelper.getCityList(countyName))
    }
}