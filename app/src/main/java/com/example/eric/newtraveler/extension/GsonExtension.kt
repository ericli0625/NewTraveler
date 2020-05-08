package com.example.eric.newtraveler.extension

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

fun <T> Gson.fromJsonOrElse(json: String, typeOfT: Type, defaultValue: () -> T): T =
        try {
            fromJson(json, typeOfT)
        } catch (e: JsonSyntaxException) {
            defaultValue()
        } catch (e: JsonParseException) {
            defaultValue()
        }