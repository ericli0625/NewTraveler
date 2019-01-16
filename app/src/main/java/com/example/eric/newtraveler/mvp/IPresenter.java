package com.example.eric.newtraveler.mvp;

public interface IPresenter {
    void showCountyList();

    void showKeywordSearchSpot(String queryString);

    void showSpotDetail(String spotName);

    void showCityList(String countyName);

    void showSpotList(String cityName);

    void preloadAllCountyAndCityList();

    void backToCityListPage();

    void showWeatherCountyList();

    void showWeatherForecast(String countyName);

    void showFavoriteList();

    void showFavoriteSpotDetail(String spotName);

    void deleteFavoriteSpot(String spotName);
}
