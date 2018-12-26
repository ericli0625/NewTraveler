package com.example.eric.newtraveler.presenter;

public interface IPresenter {
    void showCountyList();

    void showKeywordSearchSpot(String queryString);

    void showSpotDetail(String result, int position);

    void showCityList(String result, int position);

    void showSpotList(String result, int position);

    void preloadAllCountyAndCityList();
}
