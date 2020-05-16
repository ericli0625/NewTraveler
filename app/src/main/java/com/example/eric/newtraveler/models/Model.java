package com.example.eric.newtraveler.models;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eric.newtraveler.network.NetworkApi;
import com.example.eric.newtraveler.network.NetworkWeatherApi;
import com.example.eric.newtraveler.network.response.AttractionDetail;
import com.example.eric.newtraveler.network.response.WeatherInfo;
import com.example.eric.newtraveler.ui.MainActivity;
import com.example.eric.newtraveler.util.SQLiteManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Model {

    public void queryKeywordSearchSpot(@Nullable String queryString, Observer<List<String>> observer) {
        Observable<List<AttractionDetail>> observable = NetworkApi.sharedInstance().getKeywordSearchSpotDetail(queryString);
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<List<AttractionDetail>, List<String>>() {
                    @Override
                    public List<String> apply(List<AttractionDetail> list) throws Exception {
                        Log.i(MainActivity.TAG, "Model, queryKeywordSearchSpot, apply");
                        ArrayList<String> newArrayList = new ArrayList<String>();
                        for (AttractionDetail spotDetail : list) {
                            newArrayList.add(spotDetail.getName());
                        }
                        return newArrayList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void queryFavoriteList(Observer<ArrayList<String>> observer) {
        Observable<ArrayList<String>> observable = Observable.create(
                new ObservableOnSubscribe<ArrayList<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<ArrayList<String>> emitter) throws Exception {
                        Log.i(MainActivity.TAG, "Model, queryFavoriteList, subscribe");
                        queryFavoriteList();
                        emitter.onNext(queryFavoriteList());
                        emitter.onComplete();
                    }
                });
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void queryFavoriteSpotDetail(@NonNull String spotName, Observer<Bundle> observer) {
        Observable<AttractionDetail> observable = Observable.create(
                new ObservableOnSubscribe<AttractionDetail>() {
                    @Override
                    public void subscribe(ObservableEmitter<AttractionDetail> emitter) throws Exception {
                        Log.i(MainActivity.TAG, "Model, queryFavoriteSpotDetail, subscribe");
                        AttractionDetail spotDetail = getSpotDetail(spotName);
                        emitter.onNext(spotDetail);
                        emitter.onComplete();
                    }
                });
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<AttractionDetail, Bundle>() {
                    @Override
                    public Bundle apply(AttractionDetail spotDetail) throws Exception {
                        return getSpotDetailBundle(spotDetail, false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void deleteFavoriteSpot(String spotName, Observer<ArrayList<String>> observer) {
        Observable<ArrayList<String>> observable = Observable.create(
                new ObservableOnSubscribe<ArrayList<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<ArrayList<String>> emitter) throws Exception {
                        Log.i(MainActivity.TAG, "Model, deleteFavoriteSpot, subscribe");
                        SQLiteManager.getInstance().delete(spotName);
                        emitter.onNext(queryFavoriteList());
                        emitter.onComplete();
                    }
                });
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private ArrayList<String> queryFavoriteList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor = SQLiteManager.getInstance().select();
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String name = cursor.getString(1);
                arrayList.add(name);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }

    private AttractionDetail getSpotDetail(String spotName) {
        AttractionDetail spotDetail = AttractionDetail.getDefaultInstance();
//        Cursor cursor = SQLiteManager.getInstance().findSpot(spotName);
//        cursor.moveToFirst();
//        for (int i = 0; i < cursor.getCount(); i++) {
//            spotDetail.setId(cursor.getString(0));
//            spotDetail.setName(cursor.getString(1));
//            spotDetail.setCategory(cursor.getString(2));
//            spotDetail.setAddress(cursor.getString(3));
//            spotDetail.setTelephone(cursor.getString(4));
//            spotDetail.setLongitude(cursor.getString(5));
//            spotDetail.setLatitude(cursor.getString(6));
//            spotDetail.setContent(cursor.getString(7));
//            cursor.moveToNext();
//        }
//        cursor.close();
        return spotDetail;
    }

    private Bundle getSpotDetailBundle(AttractionDetail spotDetail , Boolean isShowIcon) {
        Bundle bundle = new Bundle();
        bundle.putString("id", spotDetail.getId());
        bundle.putString("name", spotDetail.getName());
        bundle.putString("city", spotDetail.getCity());
        bundle.putString("county", spotDetail.getCounty());
        bundle.putString("category", spotDetail.getCategory());
        bundle.putString("address", spotDetail.getAddress());
        bundle.putString("telephone", spotDetail.getTelephone());
        bundle.putString("longitude", spotDetail.getLongitude());
        bundle.putString("latitude", spotDetail.getLatitude());
        bundle.putString("content", spotDetail.getContent());
        bundle.putBoolean("favorite_icon", isShowIcon);
        return bundle;
    }

}
