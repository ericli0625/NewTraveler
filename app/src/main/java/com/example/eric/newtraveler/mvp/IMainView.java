package com.example.eric.newtraveler.mvp;

import android.os.Bundle;

import java.util.ArrayList;

public interface IMainView {
    void showCountyListResult(String string);

    void showCityListResult(String arrayList);

    void showSpotListResult(ArrayList<String> arrayList);

    void showKeywordSearchSpotResult(ArrayList<String> arrayList);

    void showSpotDetailResult(Bundle bundle);

    void showWeatherCountyListResult(String string);

    void showWeatherForecastResult(Bundle bundle);

    void showFavoriteListResult(String string);

    void showDeleteFavoriteResult(String result);
}
