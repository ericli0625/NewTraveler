package com.example.eric.newtraveler.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.network.response.Weather
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class WeatherViewModel(private val repository: WeatherRepository) : BaseViewModel(repository) {

    private val _showCountyListEvent by lazy { MutableLiveData<Event<List<String>>>() }
    val showCountyListEvent: LiveData<Event<List<String>>> by lazy { _showCountyListEvent }

    private val _showWeatherEvent by lazy {
        MutableLiveData<Event<Pair<ArrayList<Weather.WeatherElement>, String>>>()
    }
    val showWeatherEvent: LiveData<Event<Pair<ArrayList<Weather.WeatherElement>, String>>> by lazy {
        _showWeatherEvent
    }

    private val disposables = CompositeDisposable()

    fun onActivityCreated() {
        val cityList = repository.getCountyList()
        _showCountyListEvent.value = Event(cityList)
    }

    fun queryWeatherForecast(countyName: String) {
        disposables.add(
                repository.queryWeatherForecast(countyName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { weather ->
                            val location = weather.records.location.first()
                            val weatherElementArray = location.weatherElement
                            val locationName = location.locationName
                            _showWeatherEvent.value = Event(Pair(weatherElementArray, locationName))
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}