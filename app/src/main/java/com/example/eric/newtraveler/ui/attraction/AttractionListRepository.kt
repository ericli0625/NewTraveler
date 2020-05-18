package com.example.eric.newtraveler.ui.attraction

import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.storage.BaseRepository
import io.reactivex.Observable

class AttractionListRepository : BaseRepository() {

    fun getAttractionInfoByPlace(
            countyName: String, cityName: String
    ): Observable<List<AttractionInfo>> {
        return NetworkApi.sharedInstance().getAttractionInfoByPlace("$countyName,$cityName")
    }
}