package com.example.eric.newtraveler.ui.attraction

import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.storage.BaseRepository
import io.reactivex.Observable

class AttractionListRepository : BaseRepository() {

    fun queryAttractionDetail(
            countyName: String, cityName: String
    ): Observable<List<AttractionDetail>> {
        return NetworkApi.sharedInstance().getNormalSearchSpotDetail("$countyName,$cityName")
    }
}