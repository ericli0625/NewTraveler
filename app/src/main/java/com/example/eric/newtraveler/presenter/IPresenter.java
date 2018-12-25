package com.example.eric.newtraveler.presenter;

public interface IPresenter {
    void showCityList();

    void showKeywordSearchSpot(String queryString);

    void showSpotDetail(String result, int position);

    void showCountyList(String result, int position);

    void showSpotList(String result, int position);
}
