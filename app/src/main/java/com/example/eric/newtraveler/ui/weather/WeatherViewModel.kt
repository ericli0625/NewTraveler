package com.example.eric.newtraveler.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.disposables.CompositeDisposable

class WeatherViewModel(private val repository: WeatherRepository) : BaseViewModel(repository) {

    private val _showCountyListEvent by lazy {
        MutableLiveData<Event<List<String>>>()
    }
    val showCountyListEvent: LiveData<Event<List<String>>> by lazy {
        _showCountyListEvent
    }

    private val disposables = CompositeDisposable()

    fun onActivityCreated() {
        disposables.add(
                repository.getCountyList()
                        .subscribe {
                            _showCountyListEvent.value = Event(it)
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}