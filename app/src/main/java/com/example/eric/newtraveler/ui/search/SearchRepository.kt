package com.example.eric.newtraveler.ui.search

import com.example.eric.newtraveler.network.NetworkApi
import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.storage.BaseRepository
import io.reactivex.Observable

class SearchRepository : BaseRepository() {

    fun fetchAndActivateRemoteConfig(onTemplateKeyFetchedListener: (List<String>) -> Unit) {
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) return@addOnCompleteListener

                    val result = remoteConfig.getString(KEY_TEMPLATE)
                    val list = result.split(",")
                    onTemplateKeyFetchedListener(list)
                }
    }

    fun getAttractionInfoByName(queryString: String): Observable<List<AttractionInfo>> {
        return NetworkApi.sharedInstance().getAttractionInfoByName(queryString)
    }

    companion object {
        private const val KEY_TEMPLATE = "template"
    }
}