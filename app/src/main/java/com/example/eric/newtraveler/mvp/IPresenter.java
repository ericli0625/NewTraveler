package com.example.eric.newtraveler.mvp;

public interface IPresenter {
    void showCountyList();

    void showKeywordSearchSpot(String queryString);

    void showSpotDetail(int position);

    void showCityList(int position);

    void showSpotList(int position);

    void preloadAllCountyAndCityList();

    void backToCityListPage();

    void showWeatherCountyList();

    void showWeatherForecast(int position);

    void showFavoriteList();

    void showFavoriteSpotDetail(int position);

    void deleteFavoriteSpot(int position);
}
