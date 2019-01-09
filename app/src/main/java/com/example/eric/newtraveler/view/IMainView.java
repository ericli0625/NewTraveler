package com.example.eric.newtraveler.view;

import android.os.Bundle;

import org.json.JSONArray;

public interface IMainView {
    void showCountyListResult(String string);

    void showCityListResult(String string);

    void showSpotListResult(String string);

    void showKeywordSearchSpotResult(String string);

    void showSpotDetailResult(Bundle bundle);

    void showWeatherForecastResult(JSONArray countyName);
}
