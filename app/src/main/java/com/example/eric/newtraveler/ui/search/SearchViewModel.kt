package com.example.eric.newtraveler.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val repository: SearchRepository) : BaseViewModel(repository) {

    private val attractionList: MutableList<AttractionInfo> = mutableListOf()

    private val disposables = CompositeDisposable()

    private val _showAttractionListEvent by lazy {
        MutableLiveData<Event<List<AttractionInfo>>>()
    }
    val showAttractionListEvent: LiveData<Event<List<AttractionInfo>>> by lazy {
        _showAttractionListEvent
    }

    fun fetchAndActivateRemoteConfig() {
        repository.fetchAndActivateRemoteConfig(::getTemplateKey)
    }

    fun searchAttraction(name: String) {
        disposables.add(
                repository.getAttractionInfoByName(name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            _showAttractionListEvent.value = Event(it)
                        }
        )
    }

    private fun getTemplateKey(queries: List<String>) {
        disposables.add(
                Observable.just(queries)
                        .flatMap { Observable.fromIterable(it) }
                        .flatMap { repository.getAttractionInfoByName(it) }
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