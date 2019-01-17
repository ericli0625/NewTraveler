package com.example.eric.newtraveler.ui;

import android.os.Bundle;

import java.util.ArrayList;

public interface IMainView {
    void showCountyListResult(ArrayList<String> arrayList);

    void showCityListResult(ArrayList<String> arrayList);

    void showSpotListResult(ArrayList<String> arrayList);

    void showKeywordSearchSpotResult(ArrayList<String> arrayList);

    void showSpotDetailResult(Bundle bundle);

    void showWeatherCountyListResult(ArrayList<String> arrayList);

    void showWeatherForecastResult(Bundle bundle);

    void showFavoriteListResult(ArrayList<String> arrayList);

    void showDeleteFavoriteResult(ArrayList<String> arrayList);
}
