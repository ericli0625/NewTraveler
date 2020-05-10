package com.example.eric.newtraveler.ui.weather.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.network.response.WeatherElement
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WeatherDetailViewModel(private val repository: WeatherDetailRepository) :
        BaseViewModel(repository) {

    private val _showWeatherEvent by lazy {
        MutableLiveData<Event<List<WeatherElement>>>()
    }
    val showWeatherEvent: LiveData<Event<List<WeatherElement>>> by lazy {
        _showWeatherEvent
    }

    private val disposables = CompositeDisposable()

    fun queryWeatherForecast(countyName: String) {
        disposables.add(
                repository.queryWeatherForecast(countyName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { weather ->
                            val location = weather.records.location.first()
                            val weatherElementArray = location.weatherElement
                            _showWeatherEvent.value = Event(weatherElementArray)
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}