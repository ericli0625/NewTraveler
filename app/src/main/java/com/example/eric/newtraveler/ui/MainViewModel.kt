package com.example.eric.newtraveler.ui

import com.example.eric.newtraveler.ui.base.BaseViewModel

class MainViewModel(private val repository: MainRepository) : BaseViewModel(repository) {

    fun onCreate() {
        repository.initRemoteConfig()
    }
}