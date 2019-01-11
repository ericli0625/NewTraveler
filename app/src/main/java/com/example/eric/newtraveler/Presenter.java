package com.example.eric.newtraveler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.eric.newtraveler.mvp.IPresenter;
import com.example.eric.newtraveler.mvp.IMainView;
import com.example.eric.newtraveler.observer.IObserver;

import java.lang.ref.WeakReference;


public class Presenter implements IPresenter {

    private final static int MSG_SHOW_COUNTY_LIST_RESULT = 1;
    private final static int MSG_SHOW_CITY_LIST_RESULT = 2;
    private final static int MSG_SHOW_SPOT_LIST_RESULT = 3;
    private final static int MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT = 4;
    private final static int MSG_SHOW_WEATHER_FORECAST_RESULT = 5;
    private final static int MSG_SHOW_FAVORITE_LIST_RESULT = 6;
    private final static int MSG_SHOW_SPOT_DETAIL_RESULT = 7;

    private final Repository mRepository;
    private final IMainView mMainView;
    private final Model mModel;

    private final UIHandler mMainHandler;

    private IObserver mQueryAllCityAndCountyListObserver = new QueryAllCityAndCountyListObserver();
    private IObserver mQueryCountyListObserver = new QueryCountyListObserver();
    private IObserver mQueryCityListObserver = new QueryCityListObserver();
    private IObserver mQuerySpotListObserver = new QuerySpotListObserver();
    private IObserver mQueryKeywordSearchSpotObserver = new QueryKeywordSearchSpotObserver();
    private IObserver mQueryWeatherForecastObserver = new QueryWeatherForecastObserver();
    private IObserver mQueryFavoriteListObserver = new QueryFavoriteListObserver();
    private IObserver mQuerySpotDetailObserver = new QuerySpotDetailObserver();

    public Presenter(IMainView mainView, Repository repository) {
        this.mModel = new Model();
        this.mMainView = mainView;
        this.mRepository = repository;
        mModel.initBackgroundHandler();
        mMainHandler = new UIHandler(mainView, mModel, Looper.getMainLooper());
    }

    @Override
    public void preloadAllCountyAndCityList() {
        if (mRepository.isExistPreloadList()) {
            showCountyList();
        } else {
            mModel.addObserver(mQueryAllCityAndCountyListObserver);
            mModel.queryAllCountyAndCityList();
        }
    }

    @Override
    public void backToCityListPage() {
        int position = mModel.getNowCountyPosition();
        String countyList = mRepository.getCountyList();
        if (mRepository.isExistPreloadList()) {
            String repoCityList = mRepository.getCityList(position);
            mMainView.showCityListResult(repoCityList);
        } else {
            mModel.addObserver(mQueryCityListObserver);
            mModel.queryCityList(countyList, position);
        }
    }

    @Override
    public void showCountyList() {
        if (mRepository.isExistPreloadList()) {
            String countyList = mRepository.getCountyList();
            mMainView.showCountyListResult(countyList);
        } else {
            mModel.addObserver(mQueryCountyListObserver);
            mModel.queryCountyList();
        }
    }

    @Override
    public void showCityList(String countyList, int position) {
        mModel.setNowCountyStatus(countyList, position);
        if (mRepository.isExistPreloadList()) {
            String repoCityList = mRepository.getCityList(position);
            mMainView.showCityListResult(repoCityList);
        } else {
            mModel.addObserver(mQueryCityListObserver);
            mModel.queryCityList(countyList, position);
        }
    }

    @Override
    public void showSpotList(String cityList, int position) {
        mModel.setNowCityStatus(cityList, position);
        mModel.addObserver(mQuerySpotListObserver);
        mModel.queryNormalSearchSpot(cityList, position);
    }

    @Override
    public void showKeywordSearchSpot(String queryString) {
        mModel.addObserver(mQueryKeywordSearchSpotObserver);
        mModel.queryKeywordSearchSpot(queryString);
    }

    @Override
    public void showSpotDetail(String result, int position) {
        mModel.addObserver(mQuerySpotDetailObserver);
        mModel.querySpotDetail(result, position);
    }

    @Override
    public void showWeatherForecast(String countyList, int position) {
        String countyName = mModel.getCountyName(countyList, position);
        mModel.addObserver(mQueryWeatherForecastObserver);
        mModel.QueryWeatherForecast(countyName);
    }

    @Override
    public void showFavoriteList() {
        mModel.addObserver(mQueryFavoriteListObserver);
        mModel.QueryFavoriteList();
    }

    @Override
    public void showFavoriteSpotDetail(String result, int position) {
        mModel.addObserver(mQuerySpotDetailObserver);
        mModel.QueryFavoriteSpotDetail(result, position);
    }

    public class QueryAllCityAndCountyListObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQueryAllCityAndCountyListObserver);
            mRepository.parserAllCountyAndCityList((String) string);
            Message msg = new Message();
            msg.what = MSG_SHOW_COUNTY_LIST_RESULT;
            msg.obj = mRepository.getCountyList();
            mMainHandler.sendMessage(msg);
        }
    }

    public class QueryCountyListObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQueryCountyListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_COUNTY_LIST_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    public class QueryCityListObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQueryCityListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_CITY_LIST_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    public class QuerySpotListObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQuerySpotListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_SPOT_LIST_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    public class QueryKeywordSearchSpotObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQueryKeywordSearchSpotObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    public class QueryWeatherForecastObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQueryWeatherForecastObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_WEATHER_FORECAST_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

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

    private class QuerySpotDetailObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQuerySpotDetailObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_SPOT_DETAIL_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    private static class UIHandler extends Handler {

        private WeakReference<IMainView> mMinView;
        private WeakReference<Model> mModel;

        public UIHandler(IMainView mainView, Model model, Looper mainLooper) {
            super(mainLooper);
            mModel = new WeakReference<>(model);
            mMinView = new WeakReference<>(mainView);
        }

        @Override
        public void handleMessage(Message msg) {
            IMainView mainView = mMinView.get();
            Model model = mModel.get();
            if (mainView == null || model == null) {
                return;
            }
            // Gets the image task from the incoming Message object.
            Object result = msg.obj != null ? msg.obj : "";
            int msgType = msg.what;
            switch (msgType) {
                case MSG_SHOW_COUNTY_LIST_RESULT:
                    mainView.showCountyListResult((String) result);
                    break;
                case MSG_SHOW_CITY_LIST_RESULT:
                    mainView.showCityListResult((String) result);
                    break;
                case MSG_SHOW_SPOT_LIST_RESULT:
                    mainView.showSpotListResult((String) result);
                    break;
                case MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT:
                    mainView.showKeywordSearchSpotResult((String) result);
                    break;
                case MSG_SHOW_WEATHER_FORECAST_RESULT:
                    mainView.showWeatherForecastResult((Bundle) result);
                    break;
                case MSG_SHOW_FAVORITE_LIST_RESULT:
                    mainView.showFavoriteListResult((String) result);
                    break;
                case MSG_SHOW_SPOT_DETAIL_RESULT:
                    mainView.showSpotDetailResult((Bundle) result);
                    break;
                default:
                    break;
            }
        }

    }

}
