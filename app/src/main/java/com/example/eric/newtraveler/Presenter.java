package com.example.eric.newtraveler;

import android.util.Log;

import com.example.eric.newtraveler.presenter.IObserver;
import com.example.eric.newtraveler.presenter.IPresenter;
import com.example.eric.newtraveler.view.IMainView;


public class Presenter implements IPresenter {

    private IMainView mMainView;
    private Model mModel;
    private PresenterObserver mPresenterObserver = new PresenterObserver();

    public Presenter(IMainView mainView) {
        this.mMainView = mainView;
        this.mModel = new Model();
        mModel.initBackgroundHandler();
    }

    @Override
    public void showAllCity() {
        Log.v(MainActivity.TAG,"showAllCity");
        mModel.addObserver(mPresenterObserver);
        mModel.handleAllCounty();
    }

    public class PresenterObserver implements IObserver {
        @Override
        public void notifyResult(String string) {
            mMainView.showCityResult(string);
            mModel.removeObserver(mPresenterObserver);
        }
    }

}
