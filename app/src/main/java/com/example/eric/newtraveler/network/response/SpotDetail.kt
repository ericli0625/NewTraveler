package com.example.eric.newtraveler.network.response

import com.google.gson.annotations.SerializedName

data class SpotDetail(
        @SerializedName("id") val id: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("city") val city: String?,
        @SerializedName("county") val county: String?,
        @SerializedName("category") val category: String?,
        @SerializedName("address") val address: String?,
        @SerializedName("telephone") val telephone: String?,
        @SerializedName("longitude") val longitude: String?,
        @SerializedName("latitude") val latitude: String?,
        @SerializedName("content") val content: String?
) {
    companion object {
        @JvmStatic
        val defaultInstance = SpotDetail("", "", "", "", "", "", "", "", "", "")
    }
}