package com.example.eric.newtraveler.models;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eric.newtraveler.network.NetworkApi;
import com.example.eric.newtraveler.network.NetworkWeatherApi;
import com.example.eric.newtraveler.network.response.SpotDetail;
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

    private List<SpotDetail> mSpotDetailList;
    private String mNowCountyName;

    public Model() { }

    public void queryAllCountyAndCityList(Observer<ArrayList> observer) {

    }

    public void queryBackToCityListPage(Observer<ArrayList> observer) {
        queryCityList(getNowCountyName(), observer);
    }

    public void queryCountyList(Observer<ArrayList> observer) { }

    public void queryCityList(@NonNull String countyName, Observer<ArrayList> observer) { }

    public void queryNormalSearchSpot(@NonNull String cityName, Observer<List<String>> observer) {
        Observable<List<SpotDetail>> observable = NetworkApi.sharedInstance().getNormalSearchSpotDetail(getNowCountyName() + "," + cityName);
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<List<SpotDetail>, List<String>>() {
                    @Override
                    public List<String> apply(List<SpotDetail> list) throws Exception {
                        Log.i(MainActivity.TAG, "Model, queryNormalSearchSpot, apply");
//                        setSpotDetailList(list);
                        ArrayList<String> newArrayList = new ArrayList<String>();
                        for (SpotDetail spotDetail : list) {
                            newArrayList.add(spotDetail.getName());
                        }
                        return newArrayList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void queryKeywordSearchSpot(@Nullable String queryString, Observer<List<String>> observer) {
        Observable<List<SpotDetail>> observable = NetworkApi.sharedInstance().getKeywordSearchSpotDetail(queryString);
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<List<SpotDetail>, List<String>>() {
                    @Override
                    public List<String> apply(List<SpotDetail> list) throws Exception {
                        Log.i(MainActivity.TAG, "Model, queryKeywordSearchSpot, apply");
                        setSpotDetailList(list);
                        ArrayList<String> newArrayList = new ArrayList<String>();
                        for (SpotDetail spotDetail : list) {
                            newArrayList.add(spotDetail.getName());
                        }
                        return newArrayList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void querySpotDetail(@NonNull String spotName, Observer<Bundle> observer) {
        Observable<SpotDetail> observable = Observable.create(
                new ObservableOnSubscribe<SpotDetail>() {
                    @Override
                    public void subscribe(ObservableEmitter<SpotDetail> emitter) throws Exception {
                        Log.i(MainActivity.TAG, "Model, querySpotDetail, subscribe");
                        for (SpotDetail spotDetail : getSpotDetailList()) {
                            if (spotDetail.getName().equals(spotName)) {
                                emitter.onNext(spotDetail);
                            }
                        }
                        emitter.onComplete();
                    }
                });
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<SpotDetail, Bundle>() {
                    @Override
                    public Bundle apply(SpotDetail spotDetail) throws Exception {
                        return getSpotDetailBundle(spotDetail, true);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void queryWeatherCountyList(Observer<ArrayList> observer) {
        queryCountyList(observer);
    }

    public void queryWeatherForecast(@NonNull String countyName, Observer<Bundle> observer) {
        Observable<WeatherInfo> observable = NetworkWeatherApi.sharedInstance().getWeather(countyName, "CWB-38A07514-8234-4044-AC3D-17FE6A4320BF");
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<WeatherInfo, Bundle>() {
                    @Override
                    public Bundle apply(WeatherInfo weatherInfo) throws Exception {
                        Log.i(MainActivity.TAG, "Model, queryWeatherForecast, apply");
//                        Weather.Location location = null;
                        Bundle bundle = new Bundle();
//                        location = weather.getRecords().getLocation().get(0);
//                        ArrayList<Weather.WeatherElement> weatherElementArray = location.getWeatherElement();
//                        String locationName = location.getLocationName();
//                        bundle.putParcelableArrayList("weatherElementArray", weatherElementArray);
//                        bundle.putString("locationName", locationName);
                        return bundle;
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
        Observable<SpotDetail> observable = Observable.create(
                new ObservableOnSubscribe<SpotDetail>() {
                    @Override
                    public void subscribe(ObservableEmitter<SpotDetail> emitter) throws Exception {
                        Log.i(MainActivity.TAG, "Model, queryFavoriteSpotDetail, subscribe");
                        SpotDetail spotDetail = getSpotDetail(spotName);
                        emitter.onNext(spotDetail);
                        emitter.onComplete();
                    }
                });
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<SpotDetail, Bundle>() {
                    @Override
                    public Bundle apply(SpotDetail spotDetail) throws Exception {
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

    private String getNowCountyName() {
        return mNowCountyName;
    }

    private void setNowCountyStatus(String countyName) {
        mNowCountyName = countyName;
    }

    private void setSpotDetailList(List<SpotDetail> spotDetailList) {
        mSpotDetailList = spotDetailList;
    }

    private List<SpotDetail> getSpotDetailList() {
        return mSpotDetailList;
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

    private SpotDetail getSpotDetail(String spotName) {
        SpotDetail spotDetail = SpotDetail.getDefaultInstance();
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

    private Bundle getSpotDetailBundle(SpotDetail spotDetail , Boolean isShowIcon) {
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
