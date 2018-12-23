package com.example.eric.newtraveler;

import android.util.Log;

import com.example.eric.newtraveler.view.IObserver;
import com.example.eric.newtraveler.presenter.IPresenter;
import com.example.eric.newtraveler.view.IMainView;


public class Presenter implements IPresenter {

    private IMainView mMainView;
    private Model mModel;

    private QueryCityListObserver mQueryCityListObserver = new QueryCityListObserver();
    private QueryKeywordSearchSpotObserver mQueryKeywordSearchSpotObserver = new QueryKeywordSearchSpotObserver();

    public Presenter(IMainView mainView) {
        this.mMainView = mainView;
        this.mModel = new Model();
        mModel.initBackgroundHandler();
    }

    @Override
    public void showCityList() {
        Log.v(MainActivity.TAG, "showCityList");
        mModel.addObserver(mQueryCityListObserver);
        mModel.queryCityList();
    }

    @Override
    public void showKeywordSearchSpot(String queryString) {
        Log.v(MainActivity.TAG, "showKeywordSearchSpot");
        mModel.addObserver(mQueryKeywordSearchSpotObserver);
        mModel.queryKeywordSearchSpot(queryString);
    }

    public class QueryCityListObserver implements IObserver {
        @Override
        public void notifyResult(String string) {
            mModel.removeObserver(mQueryCityListObserver);
            mMainView.showCityListResult(string);
        }
    }

    public class QueryKeywordSearchSpotObserver implements IObserver {
        @Override
        public void notifyResult(String string) {
            mModel.removeObserver(mQueryKeywordSearchSpotObserver);
            mMainView.showKeywordSearchSpotResult(string);
        }
    }

}
