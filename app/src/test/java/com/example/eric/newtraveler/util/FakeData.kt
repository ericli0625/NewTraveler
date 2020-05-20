package com.example.eric.newtraveler.util

import com.example.eric.newtraveler.network.response.AttractionInfo

object FakeData {

    fun getFakeAttraction(): AttractionInfo {
        return getFakeAttractions().first()
    }

    fun getFakeAttractions(): List<AttractionInfo> {
        return listOf(
                AttractionInfo(
                        "1",
                        "台北美術館",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""),
                AttractionInfo(
                        "2",
                        "台北植物園",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "")
        )
    }
}