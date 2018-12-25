package com.example.eric.newtraveler;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.eric.newtraveler.presenter.IPresenter;
import com.example.eric.newtraveler.view.IMainView;
import com.example.eric.newtraveler.view.IObserver;

import java.lang.ref.WeakReference;


public class Presenter implements IPresenter {

    private final static int MSG_SHOW_CITY_LIST_RESULT = 1;
    private final static int MSG_SHOW_COUNTY_LIST_RESULT = 2;
    private final static int MSG_SHOW_SPOT_LIST_RESULT = 3;
    private final static int MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT = 4;

    private SharedPreferences mSharedPreferences;
    private IMainView mMainView;
    private Model mModel;

    private UIHandler mMainHandler;

    private IObserver mQueryCityListObserver = new QueryCityListObserver();
    private IObserver mQueryCountyListObserver = new QueryCountyListObserver();
    private IObserver mQuerySpotListObserver = new QuerySpotListObserver();
    private IObserver mQueryKeywordSearchSpotObserver = new QueryKeywordSearchSpotObserver();

    public Presenter(IMainView mainView, SharedPreferences sharedPreferences) {
        this.mModel = new Model();
        this.mMainView = mainView;
        this.mSharedPreferences = sharedPreferences;
        mModel.initBackgroundHandler();
        mMainHandler = new UIHandler(mainView, Looper.getMainLooper());
    }

    @Override
    public void showCityList() {
        Log.v(MainActivity.TAG, "Presenter, showCityList");
        if (mSharedPreferences.getString("citylist", "").equals("")) {
            mModel.addObserver(mQueryCityListObserver);
            mModel.queryCityList();
        } else {
            String string = mSharedPreferences.getString("citylist", "");
            mMainView.showCityListResult(string);
        }
    }

    @Override
    public void showCountyList(String cityList, int position) {
        mModel.addObserver(mQueryCountyListObserver);
        mModel.queryCountyList(cityList, position);
    }

    @Override
    public void showSpotList(String result, int position) {
        mModel.addObserver(mQuerySpotListObserver);
        mModel.queryNormalSearchSpot(result, position);
    }

    @Override
    public void showKeywordSearchSpot(String queryString) {
        Log.v(MainActivity.TAG, "Presenter, showKeywordSearchSpot");
        mModel.addObserver(mQueryKeywordSearchSpotObserver);
        mModel.queryKeywordSearchSpot(queryString);
    }

    @Override
    public void showSpotDetail(String result, int position) {
        Bundle bundle = mModel.getSpotDetailBundle(result, position);
        mMainView.showSpotDetailResult(bundle);
    }

    public class QueryCityListObserver implements IObserver {
        @Override
        public void notifyResult(String string) {
            if (mSharedPreferences.getString("citylist", "").equals("")) {
                mSharedPreferences.edit().putString("citylist", string).apply();
            }
            mModel.removeObserver(mQueryCityListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_CITY_LIST_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    public class QueryCountyListObserver implements IObserver {
        @Override
        public void notifyResult(String string) {
            mModel.removeObserver(mQueryCountyListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_COUNTY_LIST_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    public class QuerySpotListObserver implements IObserver {
        @Override
        public void notifyResult(String string) {
            mModel.removeObserver(mQuerySpotListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_SPOT_LIST_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    public class QueryKeywordSearchSpotObserver implements IObserver {
        @Override
        public void notifyResult(String string) {
            mModel.removeObserver(mQueryKeywordSearchSpotObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT;
            msg.obj = string;
            mMainHandler.sendMessage(msg);
        }
    }

    private static class UIHandler extends Handler {

        private WeakReference<IMainView> mMinView;

        public UIHandler(IMainView mainView, Looper mainLooper) {
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
            String string = msg.obj != null ? (String) msg.obj : "";
            int msgType = msg.what;
            switch (msgType) {
                case MSG_SHOW_CITY_LIST_RESULT:
                    mainView.showCityListResult(string);
                    break;
                case MSG_SHOW_COUNTY_LIST_RESULT:
                    mainView.showCountyListResult(string);
                    break;
                case MSG_SHOW_SPOT_LIST_RESULT:
                    mainView.showSpotListResult(string);
                    break;
                case MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT:
                    mainView.showKeywordSearchSpotResult(string);
                    break;
                default:
                    break;
            }
        }

    }

}
