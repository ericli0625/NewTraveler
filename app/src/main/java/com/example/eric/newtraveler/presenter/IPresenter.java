package com.example.eric.newtraveler.presenter;

import android.content.Context;

public interface IPresenter {
    void showCityList();

    void showKeywordSearchSpot(String queryString);

    void showSpotDetail(Context context, String result, int position);

    void showCountyList(String result, int position);
}
