package com.example.eric.newtraveler.ui.search

import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.storage.BaseRepository
import io.reactivex.Observable

class SearchRepository : BaseRepository() {

    fun getAttractionInfoByName(queryString: String): Observable<List<AttractionInfo>> {
        return NetworkApi.sharedInstance().getAttractionInfoByName(queryString)
    }
}