package com.example.eric.newtraveler.model

import com.example.eric.newtraveler.network.responseData.Weather

data class WeatherViewInfo(
        val weatherParameterList: List<List<Weather.Time>>
)