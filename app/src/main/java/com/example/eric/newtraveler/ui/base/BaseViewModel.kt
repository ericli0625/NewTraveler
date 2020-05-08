package com.example.eric.newtraveler.ui.base

import androidx.lifecycle.ViewModel
import com.example.eric.newtraveler.storage.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {

}