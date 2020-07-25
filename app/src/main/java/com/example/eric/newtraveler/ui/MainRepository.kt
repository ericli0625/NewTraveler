package com.example.eric.newtraveler.ui

import com.example.eric.newtraveler.storage.BaseRepository

class MainRepository : BaseRepository() {

    fun fetchAndActivateRemoteConfig() {
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) return@addOnCompleteListener

                    val result = remoteConfig.getString(KEY_TEMPLATE)
                    val list = result.split(",")
                    preferencesHelper.updateRemoteConfigResult(list)
                }
    }

    companion object {
        private const val KEY_TEMPLATE = "template"
    }
}