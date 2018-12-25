package com.example.eric.newtraveler.view;

import android.os.Bundle;

public interface IMainView {
    void showCityListResult(String string);

    void showCountyListResult(String string);

    void showSpotListResult(String string);

    void showKeywordSearchSpotResult(String string);

    void showSpotDetailResult(Bundle bundle);
}
