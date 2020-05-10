package com.example.eric.newtraveler.network.response

import com.google.gson.annotations.SerializedName

data class WeatherInfo(
        @SerializedName("success") val success: String,
        @SerializedName("records") val records: Records
)

data class Records(
        @SerializedName("datasetDescription") val description: String,
        @SerializedName("location") val location: List<Location>
)

data class Location(
        @SerializedName("locationName") val locationName: String,
        @SerializedName("weatherElement") val weatherElement: List<WeatherElement>
)

data class WeatherElement(
        @SerializedName("elementName") val elementName: String,
        @SerializedName("time") val time: List<Time>
)

data class Time(
        @SerializedName("startTime") val startTime: String,
        @SerializedName("endTime") val endTime: String,
        @SerializedName("parameter") val parameter: WeatherElementParameter
)

data class WeatherElementParameter(
        @SerializedName("parameterName") val name: String,
        @SerializedName("parameterValue") val value: String
)