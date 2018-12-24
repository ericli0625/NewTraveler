package com.example.eric.newtraveler;

import android.content.Context;
import android.content.Intent;
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
    private final static int MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT = 2;
    private final static int MSG_SHOW_COUNTY_LIST_RESULT = 3;

    private Model mModel;

    private UIHandler mMainHandler;

    private IObserver mQueryCityListObserver = new QueryCityListObserver();
    private IObserver mQueryKeywordSearchSpotObserver = new QueryKeywordSearchSpotObserver();
    private IObserver mQueryCountyListObserver = new QueryCountyListObserver();

    public Presenter(IMainView mainView) {
        this.mModel = new Model();
        mModel.initBackgroundHandler();
        mMainHandler = new UIHandler(mainView, Looper.getMainLooper());
    }

    @Override
    public void showCityList() {
        Log.v(MainActivity.TAG, "Presenter, showCityList");
        mModel.addObserver(mQueryCityListObserver);
        mModel.queryCityList();
    }

    @Override
    public void showKeywordSearchSpot(String queryString) {
        Log.v(MainActivity.TAG, "Presenter, showKeywordSearchSpot");
        mModel.addObserver(mQueryKeywordSearchSpotObserver);
        mModel.queryKeywordSearchSpot(queryString);
    }

    @Override
    public void showSpotDetail(Context context, String result, int position) {
        Intent intent = new Intent();
        intent.setClass(context, SpotDetailActivity.class);
        Bundle bundle = mModel.getSpotDetailBundle(result, position);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    public void showCountyList(String cityList, int position) {
        mModel.addObserver(mQueryCountyListObserver);
        mModel.queryCountyList(cityList, position);
    }

    public class QueryCityListObserver implements IObserver {
        @Override
        public void notifyResult(String string) {
            mModel.removeObserver(mQueryCityListObserver);
            Message msg = new Message();
            msg.what = MSG_SHOW_CITY_LIST_RESULT;
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
                case MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT:
                    mainView.showKeywordSearchSpotResult(string);
                    break;
                case MSG_SHOW_COUNTY_LIST_RESULT:
                    mainView.showCountyListResult(string);
                    break;
                default:
                    break;
            }
        }

    }

}
