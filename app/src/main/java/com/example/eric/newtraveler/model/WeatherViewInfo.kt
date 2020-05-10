package com.example.eric.newtraveler.model

import com.example.eric.newtraveler.network.response.Time

data class WeatherViewInfo(
        val weatherParameterList: List<List<Time>>
) {
    companion object {
        val defaultInstance = WeatherViewInfo(listOf())
    }
}