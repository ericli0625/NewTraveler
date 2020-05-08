package com.example.eric.newtraveler.extension

import android.content.SharedPreferences

fun SharedPreferences.getStringOrElse(key: String, defValue: String): String =
        getString(key, defValue) ?: defValue