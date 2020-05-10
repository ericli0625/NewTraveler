package com.example.eric.newtraveler.network.response

import com.google.gson.annotations.SerializedName

data class WeatherInfo(
        @SerializedName("success") val success: String?,
        @SerializedName("records") val records: Records?
)

data class Records(
        @SerializedName("datasetDescription") val description: String?,
        @SerializedName("location") private val _location: List<Location>?
) {
    val location: List<Location>
        get() = _location ?: listOf()
}

data class Location(
        @SerializedName("locationName") val name: String?,
        @SerializedName("weatherElement") private val _weatherElement: List<WeatherElement>?
) {
    val weatherElement: List<WeatherElement>
        get() = _weatherElement ?: listOf()

    companion object {
        val defaultInstance = Location("", listOf())
    }
}

data class WeatherElement(
        @SerializedName("elementName") val name: String?,
        @SerializedName("time") private val _time: List<Time>?
) {
    val time: List<Time>
        get() = _time ?: listOf()
}

data class Time(
        @SerializedName("startTime") val startTime: String?,
        @SerializedName("endTime") val endTime: String?,
        @SerializedName("parameter") val parameter: WeatherElementParameter?
)

data class WeatherElementParameter(
        @SerializedName("parameterName") val name: String?,
        @SerializedName("parameterValue") val value: String?
)