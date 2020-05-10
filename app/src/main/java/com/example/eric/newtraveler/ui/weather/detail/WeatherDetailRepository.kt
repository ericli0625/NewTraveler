package com.example.eric.newtraveler.ui.weather.detail

import com.example.eric.newtraveler.network.NetworkWeatherApi
import com.example.eric.newtraveler.network.response.WeatherInfo
import com.example.eric.newtraveler.storage.BaseRepository
import io.reactivex.Observable

class WeatherDetailRepository : BaseRepository() {

    fun queryWeatherForecast(countyName: String): Observable<WeatherInfo> {
        return NetworkWeatherApi.sharedInstance().getWeather(countyName, AUTH_HEADER)
    }

    companion object {
        private const val AUTH_HEADER = "CWB-38A07514-8234-4044-AC3D-17FE6A4320BF"
    }
}