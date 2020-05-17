package com.example.eric.newtraveler.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val repository: SearchRepository) : BaseViewModel(repository) {

    private val attractionList: MutableList<AttractionDetail> = mutableListOf()

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

    private fun getTemplateKey(queries: List<String>) {
        disposables.add(
                Observable.just(queries)
                        .flatMap { Observable.fromIterable(it) }
                        .flatMap { repository.getKeywordSearchSpotDetail(it) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            _showAttractionListEvent.value = Event(attractionList)
                        }
                        .subscribe {
                            attractionList += it
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}