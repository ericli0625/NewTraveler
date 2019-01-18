package com.example.eric.newtraveler.ui;

import android.os.Bundle;
import android.util.Log;

import com.example.eric.newtraveler.models.Model;
import com.example.eric.newtraveler.util.Repository;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class Presenter implements IPresenter {

    private final Model mModel;
    private final IMainView mMainView;

    public Presenter(IMainView mainView, Repository repository) {
        this.mModel = new Model(repository);
        this.mMainView = mainView;
    }

    @Override
    public void preloadAllCountyAndCityList() {
        mModel.queryAllCountyAndCityList(mAllCountyAndCityListObserver);
    }

    private Observer<ArrayList> mAllCountyAndCityListObserver = new Observer<ArrayList>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "AllCountyAndCityListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList arrayList) {
            Log.v(MainActivity.TAG, "AllCountyAndCityListObserver, onNext");
            if (arrayList != null) {
                mMainView.showCountyListResult(arrayList);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "AllCountyAndCityListObserver, onError = " + e);
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "AllCountyAndCityListObserver, onComplete");
        }
    };

    @Override
    public void backToCityListPage() {
        mModel.queryBackToCityListPage(mBackToCityListPageObserver);
    }

    private Observer<ArrayList> mBackToCityListPageObserver = new Observer<ArrayList>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "BackToCityListPageObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList arrayList) {
            Log.v(MainActivity.TAG, "BackToCityListPageObserver, onNext");
            if (arrayList != null) {
                mMainView.showCountyListResult(arrayList);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "BackToCityListPageObserver, onError = " + e);
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "BackToCityListPageObserver, onComplete");
        }
    };

    @Override
    public void showCountyList() {
        mModel.queryCountyList(mCountyListObserver);
    }

    private Observer<ArrayList> mCountyListObserver = new Observer<ArrayList>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "CountyListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList arrayList) {
            Log.v(MainActivity.TAG, "CountyListObserver, onNext");
            if (arrayList != null) {
                mMainView.showCountyListResult(arrayList);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "CountyListObserver, onError = " + e);
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "CountyListObserver, onComplete");
        }
    };

    @Override
    public void showCityList(String countyName) {
        mModel.queryCityList(countyName, mCityListObserver);
    }

    private Observer<ArrayList> mCityListObserver = new Observer<ArrayList>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "CityListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList arrayList) {
            Log.v(MainActivity.TAG, "CityListObserver, onNext");
            if (arrayList != null) {
                mMainView.showCountyListResult(arrayList);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "CityListObserver, onError = " + e);
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "CityListObserver, onComplete");
        }
    };

    @Override
    public void showSpotList(String cityName) {
        mModel.queryNormalSearchSpot(cityName, mSpotListObserver);
    }

    private Observer<ArrayList<String>> mSpotListObserver = new Observer<ArrayList<String>>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "SpotListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList<String> arrayList) {
            Log.v(MainActivity.TAG, "SpotListObserver, onNext");
            if (arrayList != null) {
                mMainView.showSpotListResult(arrayList);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "SpotListObserver, onError = " + e);
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "SpotListObserver, onComplete");
        }
    };

    @Override
    public void showKeywordSearchSpot(String queryString) {
        mModel.queryKeywordSearchSpot(queryString, mSpotListObserver);
    }

    @Override
    public void showSpotDetail(String spotName) {
        mModel.querySpotDetail(spotName, mSpotDetailObserver);
    }

    private Observer<Bundle> mSpotDetailObserver = new Observer<Bundle>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "SpotDetailObserver, onSubscribe");
        }

        @Override
        public void onNext(Bundle bundle) {
            Log.v(MainActivity.TAG, "SpotDetailObserver, onNext");
            if (bundle != null) {
                mMainView.showSpotDetailResult(bundle);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "SpotDetailObserver, onError = " + e);
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "SpotDetailObserver, onComplete");
        }
    };

    @Override
    public void showWeatherCountyList() {
        mModel.queryWeatherCountyList(mWeatherCountyListObserver);
    }

    private Observer<ArrayList> mWeatherCountyListObserver = new Observer<ArrayList>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "WeatherCountyListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList arrayList) {
            Log.v(MainActivity.TAG, "WeatherCountyListObserver, onNext");
            if (arrayList != null) {
                mMainView.showWeatherCountyListResult(arrayList);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "WeatherCountyListObserver, onError = " + e);
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "WeatherCountyListObserver, onComplete");
        }
    };

    public void showWeatherForecast(String countyName) {
        mModel.queryWeatherForecast(countyName, mWeatherForecastObserver);
    }

    private Observer<Bundle> mWeatherForecastObserver = new Observer<Bundle>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "WeatherForecastObserver, onSubscribe");
        }

        @Override
        public void onNext(Bundle bundle) {
            Log.v(MainActivity.TAG, "WeatherForecastObserver, onNext");
            if (bundle != null) {
                mMainView.showWeatherForecastResult(bundle);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "WeatherForecastObserver, onError");
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "WeatherForecastObserver, onComplete");
        }
    };

    @Override
    public void showFavoriteList() {
        mModel.queryFavoriteList(mFavoriteListObserver);
    }

    private Observer<ArrayList<String>> mFavoriteListObserver = new Observer<ArrayList<String>>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "FavoriteListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList<String> arrayList) {
            Log.v(MainActivity.TAG, "FavoriteListObserver, onNext");
            if (arrayList != null) {
                mMainView.showFavoriteListResult(arrayList);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "FavoriteListObserver, onError");
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "FavoriteListObserver, onComplete");
        }
    };

    @Override
    public void showFavoriteSpotDetail(String spotName) {
        mModel.queryFavoriteSpotDetail(spotName, mSpotDetailObserver);
    }

    @Override
    public void deleteFavoriteSpot(String spotName) {
        mModel.deleteFavoriteSpot(spotName, mDeleteFavoriteObserver);
    }

    private Observer<ArrayList<String>> mDeleteFavoriteObserver = new Observer<ArrayList<String>>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "DeleteFavoriteObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList<String> arrayList) {
            Log.v(MainActivity.TAG, "DeleteFavoriteObserver, onNext");
            if (arrayList != null) {
                mMainView.showDeleteFavoriteResult(arrayList);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "DeleteFavoriteObserver, onError");
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "DeleteFavoriteObserver, onComplete");
        }
    };

}
