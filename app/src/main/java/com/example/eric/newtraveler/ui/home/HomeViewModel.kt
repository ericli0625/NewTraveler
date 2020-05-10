package com.example.eric.newtraveler.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val repository: HomeRepository) : BaseViewModel(repository) {

    private var countyName: String = ""

    private val _showCountyListEvent by lazy {
        MutableLiveData<Event<List<String>>>()
    }
    val showCountyListEvent: LiveData<Event<List<String>>> by lazy {
        _showCountyListEvent
    }

    private val _showCityListEvent by lazy {
        MutableLiveData<Event<List<String>>>()
    }
    val showCityListEvent: LiveData<Event<List<String>>> by lazy {
        _showCityListEvent
    }

    private val _showAttractionListEvent by lazy {
        MutableLiveData<Event<Pair<String, String>>>()
    }
    val showAttractionListEvent: LiveData<Event<Pair<String, String>>> by lazy {
        _showAttractionListEvent
    }

    private val disposables = CompositeDisposable()

    fun onActivityCreated() {
        disposables.add(
                repository.queryLocationList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            _showCountyListEvent.value = Event(it)
                        }
        )
    }

    fun showCardItems(locationName: String) {
        val getCityListS = repository.getCityList(locationName)
        val getCountyListS = repository.getCountyList()

        disposables.add(
                Observables.zip(getCityListS, getCountyListS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            if (it.second.contains(locationName)) {
                                countyName = locationName
                                _showCityListEvent.value = Event(it.first)
                            } else {
                                _showAttractionListEvent.value =
                                        Event(Pair(countyName, locationName))
                            }
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}