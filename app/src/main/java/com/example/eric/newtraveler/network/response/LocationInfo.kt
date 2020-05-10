package com.example.eric.newtraveler.network.response

import com.google.gson.annotations.SerializedName

data class LocationInfo(
        @SerializedName("success") val id: String?,
        @SerializedName("city") private val _city: String?,
        @SerializedName("county") private val _county: String?
) {
    val city: String
        get() = _city ?: ""

    val county: String
        get() = _county ?: ""
}