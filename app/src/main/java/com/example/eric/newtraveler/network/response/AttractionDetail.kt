package com.example.eric.newtraveler.network.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "attractions")
data class AttractionDetail(
        @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") val id: String,
        @SerializedName("name") private val _name: String?,
        @SerializedName("city") private val _city: String?,
        @SerializedName("county") private val _county: String?,
        @SerializedName("category") private val _category: String?,
        @SerializedName("address") private val _address: String?,
        @SerializedName("telephone") private val _telephone: String?,
        @SerializedName("longitude") private val _longitude: String?,
        @SerializedName("latitude") private val _latitude: String?,
        @SerializedName("content") private val _content: String?
) : Parcelable {

    val name: String
        get() = _name ?: ""

    val city: String
        get() = _city ?: ""

    val county: String
        get() = _county ?: ""

    val category: String
        get() = _category ?: ""

    val address: String
        get() = _address ?: ""

    val telephone: String
        get() = _telephone ?: ""

    val longitude: String
        get() = _longitude ?: ""

    val latitude: String
        get() = _latitude ?: ""

    val content: String
        get() = _content ?: ""

    companion object {
        @JvmStatic
        val defaultInstance = AttractionDetail("", "", "", "", "", "", "", "", "", "")
    }
}