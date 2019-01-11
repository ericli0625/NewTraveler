package com.example.eric.newtraveler.mvp;

public interface IPresenter {
    void showCountyList();

    void showKeywordSearchSpot(String queryString);

    void showSpotDetail(String result, int position);

    void showCityList(String result, int position);

    void showSpotList(String result, int position);

    void preloadAllCountyAndCityList();

    void backToCityListPage();

    void showWeatherForecast(String result, int position);

    void showFavoriteList();

    void showFavoriteSpotDetail(String result, int position);
}
