package com.example.eric.newtraveler.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val repository: HomeRepository) : BaseViewModel(repository) {

    private var allCityList: MutableList<List<String>> = mutableListOf()

    private val _showCountyListEvent by lazy {
        MutableLiveData<Event<Pair<List<String>, List<List<String>>>>>()
    }
    val showCountyListEvent: LiveData<Event<Pair<List<String>, List<List<String>>>>> by lazy {
        _showCountyListEvent
    }

    private val _queryLocationList by lazy {
        MutableLiveData<Event<Unit>>()
    }
    val queryLocationList: LiveData<Event<Unit>> by lazy {
        _queryLocationList
    }

    private val disposables = CompositeDisposable()

    fun onActivityCreated() {
        disposables.add(
                repository.queryLocationList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            _queryLocationList.value = Event(it)
                        }
        )
    }

    fun showCountyList() {
        val countyList = repository.getCountyList()
        disposables.add(
                Observable.just(countyList)
                        .flatMap { Observable.fromIterable(it) }
                        .map(repository::getCityList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            _showCountyListEvent.value = Event(Pair(countyList, allCityList))
                        }
                        .subscribe {
                            allCityList.add(it)
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}