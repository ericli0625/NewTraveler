package com.example.eric.newtraveler.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val repository: SearchRepository) : BaseViewModel(repository) {

    private val disposables = CompositeDisposable()

    private val _showAttractionListEvent by lazy {
        MutableLiveData<Event<List<AttractionDetail>>>()
    }
    val showAttractionListEvent: LiveData<Event<List<AttractionDetail>>> by lazy {
        _showAttractionListEvent
    }

    fun fetchAndActivateRemoteConfig() {
        repository.fetchAndActivateRemoteConfig(::getTemplateKey)
    }

    private fun getTemplateKey(key: String) {
        disposables.add(
                repository.getKeywordSearchSpotDetail(key)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            _showAttractionListEvent.value = Event(it)
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}