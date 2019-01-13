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
    private final static int MSG_DELETE_FAVORITE_SPOT = 8;
    private final static int MSG_SHOW_WEATHER_COUNTY_LIST_RESULT = 9;

    private final IMainView mMainView;
    private final Model mModel;

    private final UIHandler mMainHandler;

    private IObserver mQueryAllCityAndCountyListObserver = new QueryAllCityAndCountyListObserver();
    private IObserver mQueryCountyListObserver = new QueryCountyListObserver();
    private IObserver mQueryCityListObserver = new QueryCityListObserver();
    private IObserver mQuerySpotListObserver = new QuerySpotListObserver();
    private IObserver mQueryKeywordSearchSpotObserver = new QueryKeywordSearchSpotObserver();
    private IObserver mQueryWeatherCountyListObserver = new QueryWeatherCountyListObserver();
    private IObserver mQueryWeatherForecastObserver = new QueryWeatherForecastObserver();
    private IObserver mQueryFavoriteListObserver = new QueryFavoriteListObserver();
    private IObserver mQuerySpotDetailObserver = new QuerySpotDetailObserver();
    private IObserver mDeleteFavoriteSpotObserver = new DeleteFavoriteSpotObserver();

    public Presenter(IMainView mainView, Repository repository) {
        this.mModel = new Model(repository);
        this.mMainView = mainView;
        mModel.initBackgroundHandler();
        mMainHandler = new UIHandler(mainView, mModel, Looper.getMainLooper());
    }

    @Override
    public void preloadAllCountyAndCityList() {
        mModel.addObserver(mQueryAllCityAndCountyListObserver);
        mModel.queryAllCountyAndCityList();
    }

    @Override
    public void backToCityListPage() {
        mModel.addObserver(mQueryCityListObserver);
        mModel.backToCityListPage();
    }

    @Override
    public void showCountyList() {
        mModel.addObserver(mQueryCountyListObserver);
        mModel.queryCountyList();
    }

    @Override
    public void showCityList(int position) {
        mModel.addObserver(mQueryCityListObserver);
        mModel.queryCityList(position);
    }

    @Override
    public void showSpotList(int position) {
        mModel.addObserver(mQuerySpotListObserver);
        mModel.queryNormalSearchSpot(position);
    }

    @Override
    public void showKeywordSearchSpot(String queryString) {
        mModel.addObserver(mQueryKeywordSearchSpotObserver);
        mModel.queryKeywordSearchSpot(queryString);
    }

    @Override
    public void showSpotDetail(int position) {
        mModel.addObserver(mQuerySpotDetailObserver);
        mModel.querySpotDetail(position);
    }

    @Override
    public void showWeatherCountyList() {
        mModel.addObserver(mQueryWeatherCountyListObserver);
        mModel.queryWeatherCountyList();
    }

    @Override
    public void showWeatherForecast(int position) {
        mModel.addObserver(mQueryWeatherForecastObserver);
        mModel.QueryWeatherForecast(position);
    }

    @Override
    public void showFavoriteList() {
        mModel.addObserver(mQueryFavoriteListObserver);
        mModel.QueryFavoriteList();
    }

    @Override
    public void showFavoriteSpotDetail(int position) {
        mModel.addObserver(mQuerySpotDetailObserver);
        mModel.QueryFavoriteSpotDetail(position);
    }

    @Override
    public void deleteFavoriteSpot(int position) {
        mModel.addObserver(mDeleteFavoriteSpotObserver);
        mModel.DeleteFavoriteSpot(position);
    }

    public class QueryAllCityAndCountyListObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQueryAllCityAndCountyListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_COUNTY_LIST_RESULT;
            msg.obj = string;
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

    public class QueryWeatherCountyListObserver implements IObserver {
        @Override
        public <T> void notifyResult(T string) {
            mModel.removeObserver(mQueryWeatherCountyListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_WEATHER_COUNTY_LIST_RESULT;
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
                case MSG_SHOW_WEATHER_COUNTY_LIST_RESULT:
                    mainView.showWeatherCountyListResult((String) result);
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
                case MSG_DELETE_FAVORITE_SPOT:
                    mainView.showDeleteFavoriteResult((String) result);
                    break;
                default:
                    break;
            }
        }

    }

}
