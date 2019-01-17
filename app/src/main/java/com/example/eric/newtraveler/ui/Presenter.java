package com.example.eric.newtraveler.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.eric.newtraveler.models.Model;
import com.example.eric.newtraveler.network.responseData.SpotDetail;
import com.example.eric.newtraveler.network.responseData.TravelCountyAndCity;
import com.example.eric.newtraveler.network.responseData.Weather;
import com.example.eric.newtraveler.observer.IObserver;
import com.example.eric.newtraveler.util.Repository;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class Presenter implements IPresenter {

    private final static int MSG_SHOW_COUNTY_LIST_RESULT = 1;
    private final static int MSG_SHOW_CITY_LIST_RESULT = 2;
    private final static int MSG_SHOW_SPOT_LIST_RESULT = 3;
    private final static int MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT = 4;
    private final static int MSG_SHOW_WEATHER_FORECAST_RESULT = 5;
    private final static int MSG_SHOW_FAVORITE_LIST_RESULT = 6;
    private final static int MSG_SHOW_SPOT_DETAIL_RESULT = 7;
    private final static int MSG_DELETE_FAVORITE_SPOT = 8;
    private final static int MSG_SHOW_WEATHER_COUNTY_LIST_RESULT = 9;

    private final Repository mRepository;
    private final Model mModel;

    private final UIHandler mMainHandler;
    private final IMainView mMainView;

//    private IObserver mQueryAllCityAndCountyListObserver = new QueryAllCityAndCountyListObserver();
//    private IObserver mQueryCountyListObserver = new QueryCountyListObserver();
//    private IObserver mQueryCityListObserver = new QueryCityListObserver();
//    private IObserver mQuerySpotListObserver = new QuerySpotListObserver();
//    private IObserver mQueryKeywordSearchSpotObserver = new QueryKeywordSearchSpotObserver();
//    private IObserver mQueryWeatherCountyListObserver = new QueryWeatherCountyListObserver();
//    private IObserver mQueryWeatherForecastObserver = new QueryWeatherForecastObserver();
    private IObserver mQueryFavoriteListObserver = new QueryFavoriteListObserver();
//    private IObserver mQuerySpotDetailObserver = new QuerySpotDetailObserver();
    private IObserver mDeleteFavoriteSpotObserver = new DeleteFavoriteSpotObserver();

    public Presenter(IMainView mainView, Repository repository) {
        this.mModel = new Model();
        this.mRepository = repository;
        mModel.initBackgroundHandler();
        this.mMainView = mainView;
        mMainHandler = new UIHandler(mainView, mModel, Looper.getMainLooper());
    }

    @Override
    public void preloadAllCountyAndCityList() {
//        mModel.addObserver(mQueryAllCityAndCountyListObserver);
        if (mRepository.isExistPreloadList()) {
            mModel.queryCountyList(mCountyListObserver);
        } else {
            mModel.queryAllCountyAndCityList(mAllCountyAndCityListObserver);
        }
    }

    private Observer<ArrayList<TravelCountyAndCity>> mAllCountyAndCityListObserver = new Observer<ArrayList<TravelCountyAndCity>>() {

        private ArrayList repoCityList;

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "SpotListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList<TravelCountyAndCity> arrayList) {
            Log.v(MainActivity.TAG, "SpotListObserver, onNext");
            if (arrayList != null) {
                mRepository.parserAllCountyAndCityList(arrayList);
                repoCityList = mRepository.getCountyList();
            }
            mMainView.showCountyListResult(repoCityList);
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
    public void backToCityListPage() {
//        mModel.addObserver(mQueryCityListObserver);
        String countyName = mModel.getNowCountyName();
        if (mRepository.isExistPreloadList()) {
            ArrayList repoCityList = mRepository.getCityList(countyName);
            mMainView.showCityListResult(repoCityList);
        } else {
            mModel.backToCityListPage(countyName, mCityListObserver);
        }
    }

    @Override
    public void showCountyList() {
//        mModel.addObserver(mQueryCountyListObserver);
        if (mRepository.isExistPreloadList()) {
            ArrayList repoCountyList = mRepository.getCountyList();
            mMainView.showCountyListResult(repoCountyList);
        } else {
            mModel.queryCountyList(mCountyListObserver);
        }
    }

    private Observer<ArrayList> mCountyListObserver = new Observer<ArrayList>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "CountyListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList arrayList) {
            Log.v(MainActivity.TAG, "CountyListObserver, onNext");
            mMainView.showCountyListResult(arrayList);
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
//        mModel.addObserver(mQueryCityListObserver);
        mModel.setNowCountyStatus(countyName);
        if (mRepository.isExistPreloadList()) {
            ArrayList repoCityList = mRepository.getCityList(countyName);
            mMainView.showCityListResult(repoCityList);
        } else {
            mModel.queryCityList(countyName, mCityListObserver);
        }
    }

    private Observer<ArrayList> mCityListObserver = new Observer<ArrayList>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "CountyListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList arrayList) {
            Log.v(MainActivity.TAG, "CountyListObserver, onNext");
            mMainView.showCountyListResult(arrayList);
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
    public void showSpotList(String cityName) {
//        mModel.addObserver(mQuerySpotListObserver);
        mModel.setNowCityStatus(cityName);
        mModel.queryNormalSearchSpot(cityName, mSpotListObserver);
    }

    private Observer<ArrayList<SpotDetail>> mSpotListObserver = new Observer<ArrayList<SpotDetail>>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "SpotListObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList<SpotDetail> mSpotDetailList) {
            Log.v(MainActivity.TAG, "SpotListObserver, onNext");
            ArrayList<String> newArrayList = new ArrayList<String>();
            for (SpotDetail spotDetail : mSpotDetailList) {
                newArrayList.add(spotDetail.getName());
            }
            mMainView.showSpotListResult(newArrayList);
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
//        mModel.addObserver(mQueryKeywordSearchSpotObserver);
        mModel.queryKeywordSearchSpot(queryString, mKeywordSearchSpotObserver);
    }

    private Observer<ArrayList<SpotDetail>> mKeywordSearchSpotObserver = new Observer<ArrayList<SpotDetail>>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "KeywordSearchSpotObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList<SpotDetail> mSpotDetailList) {
            Log.v(MainActivity.TAG, "KeywordSearchSpotObserver, onNext");
            ArrayList<String> newArrayList = new ArrayList<String>();
            for (SpotDetail spotDetail : mSpotDetailList) {
                newArrayList.add(spotDetail.getName());
            }
            mMainView.showSpotListResult(newArrayList);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(MainActivity.TAG, "KeywordSearchSpotObserver, onError = " + e);
        }

        @Override
        public void onComplete() {
            Log.v(MainActivity.TAG, "KeywordSearchSpotObserver, onComplete");
        }
    };


    @Override
    public void showSpotDetail(String spotName) {
//        mModel.addObserver(mQuerySpotDetailObserver);
        mModel.querySpotDetail(spotName, mSpotDetailObserver);
    }

    private Observer<ArrayList<SpotDetail>> mSpotDetailObserver = new Observer<ArrayList<SpotDetail>>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "SpotDetailObserver, onSubscribe");
        }

        @Override
        public void onNext(ArrayList<SpotDetail> spotDetail) {
            Log.v(MainActivity.TAG, "SpotDetailObserver, onNext");

            Bundle bundle = new Bundle();
            bundle.putString("id", spotDetail.get(0).getId());
            bundle.putString("name", spotDetail.get(0).getName());
            bundle.putString("city", spotDetail.get(0).getCity());
            bundle.putString("county", spotDetail.get(0).getCounty());
            bundle.putString("category", spotDetail.get(0).getCategory());
            bundle.putString("address", spotDetail.get(0).getAddress());
            bundle.putString("telephone", spotDetail.get(0).getTelephone());
            bundle.putString("longitude", spotDetail.get(0).getLongitude());
            bundle.putString("latitude", spotDetail.get(0).getLatitude());
            bundle.putString("content", spotDetail.get(0).getContent());
            bundle.putBoolean("favorite", false);

            mMainView.showSpotDetailResult(bundle);
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
//        mModel.addObserver(mQueryWeatherCountyListObserver);
        if (mRepository.isExistPreloadList()) {
            ArrayList repoCountyList = mRepository.getCountyList();
            mMainView.showWeatherCountyListResult(repoCountyList);
        } else {
            mModel.queryWeatherCountyList(mCountyListObserver);
        }
    }

    public void showWeatherForecast(String countyName) {
//        mModel.addObserver(mQueryWeatherForecastObserver);
        mModel.QueryWeatherForecast(countyName, mWeatherForecastObserver);
    }

    private Observer<Weather> mWeatherForecastObserver = new Observer<Weather>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.v(MainActivity.TAG, "WeatherForecastObserver, onSubscribe");
        }

        @Override
        public void onNext(Weather weather) {
            Log.v(MainActivity.TAG, "WeatherForecastObserver, onNext");
            Weather.Location location = null;
            Bundle bundle = new Bundle();
            if (weather != null) {
                location = weather.getRecords().getLocation().get(0);
                ArrayList<Weather.WeatherElement> weatherElementArray = location.getWeatherElement();
                String locationName = location.getLocationName();
                bundle.putParcelableArrayList("weatherElementArray", weatherElementArray);
                bundle.putString("locationName", locationName);
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
        mModel.addObserver(mQueryFavoriteListObserver);
        mModel.QueryFavoriteList();
    }

    @Override
    public void showFavoriteSpotDetail(String spotName) {
//        mModel.addObserver(mQuerySpotDetailObserver);
        mModel.QueryFavoriteSpotDetail(spotName, mSpotDetailObserver);
    }

    @Override
    public void deleteFavoriteSpot(String spotName) {
        mModel.addObserver(mDeleteFavoriteSpotObserver);
        mModel.DeleteFavoriteSpot(spotName);
    }

//    public class QueryAllCityAndCountyListObserver implements IObserver {
//        @Override
//        public <T> void notifyResult(T string) {
//            mModel.removeObserver(mQueryAllCityAndCountyListObserver);
//            Message msg = new Message();
//            msg.what = MSG_SHOW_COUNTY_LIST_RESULT;
//            msg.obj = string;
//            mMainHandler.sendMessage(msg);
//        }
//    }

//    public class QueryCountyListObserver implements IObserver {
//        @Override
//        public <T> void notifyResult(T string) {
//            mModel.removeObserver(mQueryCountyListObserver);
//            Message msg = new Message();
//            msg.what = MSG_SHOW_COUNTY_LIST_RESULT;
//            msg.obj = string;
//            mMainHandler.sendMessage(msg);
//        }
//    }

//    public class QueryCityListObserver implements IObserver {
//        @Override
//        public <T> void notifyResult(T string) {
//            mModel.removeObserver(mQueryCityListObserver);
//            Message msg = new Message();
//            msg.what = MSG_SHOW_CITY_LIST_RESULT;
//            msg.obj = string;
//            mMainHandler.sendMessage(msg);
//        }
//    }

//    public class QuerySpotListObserver implements IObserver {
//        @Override
//        public <T> void notifyResult(T result) {
//            mModel.removeObserver(mQuerySpotListObserver);
//            Message msg = new Message();
//            msg.what = MSG_SHOW_SPOT_LIST_RESULT;
//            msg.obj = result;
//            mMainHandler.sendMessage(msg);
//        }
//    }

//    public class QueryKeywordSearchSpotObserver implements IObserver {
//        @Override
//        public <T> void notifyResult(T result) {
//            mModel.removeObserver(mQueryKeywordSearchSpotObserver);
//            Message msg = new Message();
//            msg.what = MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT;
//            msg.obj = result;
//            mMainHandler.sendMessage(msg);
//        }
//    }

//    public class QueryWeatherCountyListObserver implements IObserver {
//        @Override
//        public <T> void notifyResult(T string) {
//            mModel.removeObserver(mQueryWeatherCountyListObserver);
//            Message msg = new Message();
//            msg.what = MSG_SHOW_WEATHER_COUNTY_LIST_RESULT;
//            msg.obj = string;
//            mMainHandler.sendMessage(msg);
//        }
//    }

//    public class QueryWeatherForecastObserver implements IObserver {
//        @Override
//        public <T> void notifyResult(T string) {
//            mModel.removeObserver(mQueryWeatherForecastObserver);
//            Message msg = new Message();
//            msg.what = MSG_SHOW_WEATHER_FORECAST_RESULT;
//            msg.obj = string;
//            mMainHandler.sendMessage(msg);
//        }
//    }

    public class QueryFavoriteListObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQueryFavoriteListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_FAVORITE_LIST_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

//    private class QuerySpotDetailObserver implements IObserver {
//        @Override
//        public <T> void notifyResult(T string) {
//            mModel.removeObserver(mQuerySpotDetailObserver);
//            Message msg = new Message();
//            msg.what = MSG_SHOW_SPOT_DETAIL_RESULT;
//            msg.obj = string;
//            mMainHandler.sendMessage(msg);
//        }
//    }

    private class DeleteFavoriteSpotObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mDeleteFavoriteSpotObserver);
            Message msg = new Message();
            msg.what = MSG_DELETE_FAVORITE_SPOT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    private static class UIHandler extends Handler {

        private WeakReference<IMainView> mMinView;

        public UIHandler(IMainView mainView, Model model, Looper mainLooper) {
            super(mainLooper);
            mMinView = new WeakReference<>(mainView);
        }

        @Override
        public void handleMessage(Message msg) {
            IMainView mainView = mMinView.get();
            if (mainView == null) {
                return;
            }
            // Gets the image task from the incoming Message object.
            Object result = msg.obj != null ? msg.obj : "";
            int msgType = msg.what;
            switch (msgType) {
//                case MSG_SHOW_COUNTY_LIST_RESULT:
//                    mainView.showCountyListResult((ArrayList<String>) result);
//                    break;
//                case MSG_SHOW_CITY_LIST_RESULT:
//                    mainView.showCityListResult((ArrayList<String>) result);
//                    break;
//                case MSG_SHOW_SPOT_LIST_RESULT:
//                    mainView.showSpotListResult((ArrayList<String>) result);
//                    break;
//                case MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT:
//                    mainView.showKeywordSearchSpotResult((ArrayList<String>) result);
//                    break;
//                case MSG_SHOW_WEATHER_COUNTY_LIST_RESULT:
//                    mainView.showWeatherCountyListResult((ArrayList<String>) result);
//                    break;
//                case MSG_SHOW_WEATHER_FORECAST_RESULT:
//                    mainView.showWeatherForecastResult((Bundle) result);
//                    break;
                case MSG_SHOW_FAVORITE_LIST_RESULT:
                    mainView.showFavoriteListResult((ArrayList<String>) result);
                    break;
//                case MSG_SHOW_SPOT_DETAIL_RESULT:
//                    mainView.showSpotDetailResult((Bundle) result);
//                    break;
                case MSG_DELETE_FAVORITE_SPOT:
                    mainView.showDeleteFavoriteResult((ArrayList<String>) result);
                    break;
                default:
                    break;
            }
        }

    }

}
