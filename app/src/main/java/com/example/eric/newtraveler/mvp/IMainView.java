package com.example.eric.newtraveler.mvp;

import android.database.Cursor;
import android.os.Bundle;

public interface IMainView {
    void showCountyListResult(String string);

    void showCityListResult(String string);

    void showSpotListResult(String string);

    void showKeywordSearchSpotResult(String string);

    void showSpotDetailResult(Bundle bundle);

    void showWeatherForecastResult(Bundle bundle);

    void showFavoriteListResult(Bundle bundle);
}
