package com.example.eric.newtraveler.ui.search

import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.storage.BaseRepository
import io.reactivex.Observable

class SearchRepository : BaseRepository() {

    fun fetchAndActivateRemoteConfig(onTemplateKeyFetchedListener: (String) -> Unit) {
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) return@addOnCompleteListener

                    onTemplateKeyFetchedListener(remoteConfig.getString(KEY_TEMPLATE))
                }
    }

    fun getKeywordSearchSpotDetail(queryString: String): Observable<List<AttractionDetail>> {
        return NetworkApi.sharedInstance().getKeywordSearchSpotDetail(queryString)
    }

    companion object {
        private const val KEY_TEMPLATE = "template"
    }
}