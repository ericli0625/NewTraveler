package com.example.eric.newtraveler.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val repository: HomeRepository) : BaseViewModel(repository) {

    private val _showCountyListEvent by lazy { MutableLiveData<Event<List<String>>>() }
    val showCountyListEvent: LiveData<Event<List<String>>> by lazy { _showCountyListEvent }

    private val _showCityListEvent by lazy { MutableLiveData<Event<List<String>>>() }
    val showCityListEvent: LiveData<Event<List<String>>> by lazy { _showCityListEvent }

    private val disposables = CompositeDisposable()

    fun onActivityCreated() {
        disposables.add(
                repository.queryAllCountyAndCityList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            _showCountyListEvent.value = Event(it)
                        }
        )
    }

    fun getCityList(countyName: String) {
        val cityList = repository.getCityList(countyName)
        _showCityListEvent.value = Event(cityList)
    }

    override fun onCleared() {
        disposables.dispose()
    }
}