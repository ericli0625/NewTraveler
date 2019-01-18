package com.example.eric.newtraveler.models;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.eric.newtraveler.network.ITravelService;
import com.example.eric.newtraveler.network.IWeatherService;
import com.example.eric.newtraveler.network.responseData.SpotDetail;
import com.example.eric.newtraveler.network.responseData.TravelCountyAndCity;
import com.example.eric.newtraveler.network.responseData.Weather;
import com.example.eric.newtraveler.ui.MainActivity;
import com.example.eric.newtraveler.util.Repository;
import com.example.eric.newtraveler.util.SQLiteManager;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model {

    private final Repository mRepository;

    private ArrayList<SpotDetail> mSpotDetailList;
    private String mNowCountyName;

    public Model(Repository repository) {
        this.mRepository = repository;
    }

    @NonNull
    private ITravelService getRetrofitTravelService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://travelplanbackend.herokuapp.com/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ITravelService.class);
    }

    @NonNull
    private IWeatherService getRetrofitWeatherService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opendata.cwb.gov.tw/api/v1/rest/datastore/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(IWeatherService.class);
    }

    public void queryAllCountyAndCityList(Observer<ArrayList> observer) {
        if (mRepository.isExistPreloadList()) {
            queryCountyList(observer);
        } else {
            ITravelService travelRequest = getRetrofitTravelService();
            Observable<ArrayList<TravelCountyAndCity>> observable = travelRequest.getAllCountyAndCityList();
            observable.subscribeOn(Schedulers.newThread())
                    .map(new Function<ArrayList<TravelCountyAndCity>, ArrayList>() {
                        @Override
                        public ArrayList apply(ArrayList<TravelCountyAndCity> arrayList) throws Exception {
                            mRepository.parserAllCountyAndCityList(arrayList);
                            Log.i(MainActivity.TAG, "Model, queryAllCountyAndCityList, apply");
                            return mRepository.getCountyList();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void queryBackToCityListPage(Observer<ArrayList> observer) {
        String countyName = getNowCountyName();
        queryCityList(countyName, observer);
    }

    public void queryCountyList(Observer<ArrayList> observer) {
        Observable<ArrayList> observable;
        if (mRepository.isExistPreloadList()) {
            observable = Observable.create(
                    new ObservableOnSubscribe<ArrayList>() {
                        @Override
                        public void subscribe(ObservableEmitter<ArrayList> emitter) throws Exception {
                            ArrayList repoCountyList = mRepository.getCountyList();
                            emitter.onNext(repoCountyList);
                            emitter.onComplete();
                            Log.i(MainActivity.TAG, "Model, queryCountyList, subscribe");
                        }
                    });
        } else {
            ITravelService travelRequest = getRetrofitTravelService();
            observable = travelRequest.getAllCountyList();
        }
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void queryCityList(@NonNull String countyName, Observer<ArrayList> observer) {
        setNowCountyStatus(countyName);
        Observable<ArrayList> observable;
        if (mRepository.isExistPreloadList()) {
            observable = Observable.create(
                    new ObservableOnSubscribe<ArrayList>() {
                        @Override
                        public void subscribe(ObservableEmitter<ArrayList> emitter) throws Exception {
                            ArrayList repoCityList = mRepository.getCityList(countyName);
                            emitter.onNext(repoCityList);
                            emitter.onComplete();
                            Log.i(MainActivity.TAG, "Model, queryCityList, subscribe");
                        }
                    });
        } else {
            ITravelService travelRequest = getRetrofitTravelService();
            observable = travelRequest.getCityList(countyName);
        }
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void queryNormalSearchSpot(@NonNull String cityName, Observer<ArrayList<String>> observer) {
        ITravelService travelRequest = getRetrofitTravelService();
        Observable<ArrayList<SpotDetail>> observable = travelRequest.getNormalSearchSpotDetail(getNowCountyName() + "," + cityName);
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<ArrayList<SpotDetail>, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> apply(ArrayList<SpotDetail> list) throws Exception {
                        setSpotDetailList(list);
                        ArrayList<String> newArrayList = new ArrayList<String>();
                        for (SpotDetail spotDetail : list) {
                            newArrayList.add(spotDetail.getName());
                        }
                        Log.i(MainActivity.TAG, "Model, queryNormalSearchSpot, apply");
                        return newArrayList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void queryKeywordSearchSpot(@Nullable String queryString, Observer<ArrayList<String>> observer) {
        ITravelService travelRequest = getRetrofitTravelService();
        Observable<ArrayList<SpotDetail>> observable = travelRequest.getKeywordSearchSpotDetail(queryString);
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<ArrayList<SpotDetail>, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> apply(ArrayList<SpotDetail> list) throws Exception {
                        setSpotDetailList(list);
                        ArrayList<String> newArrayList = new ArrayList<String>();
                        for (SpotDetail spotDetail : list) {
                            newArrayList.add(spotDetail.getName());
                        }
                        Log.i(MainActivity.TAG, "Model, queryNormalSearchSpot, apply");
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
                        for (SpotDetail spotDetail : getSpotDetailList()) {
                            if (spotDetail.getName().equals(spotName)) {
                                emitter.onNext(spotDetail);
                            }
                        }
                        emitter.onComplete();
                        Log.i(MainActivity.TAG, "Model, querySpotDetail, subscribe");
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
        IWeatherService weatherService = getRetrofitWeatherService();
        Observable<Weather> observable = weatherService.getWeather(countyName, "CWB-38A07514-8234-4044-AC3D-17FE6A4320BF");
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<Weather, Bundle>() {
                    @Override
                    public Bundle apply(Weather weather) throws Exception {
                        Weather.Location location = null;
                        Bundle bundle = new Bundle();
                        location = weather.getRecords().getLocation().get(0);
                        ArrayList<Weather.WeatherElement> weatherElementArray = location.getWeatherElement();
                        String locationName = location.getLocationName();
                        bundle.putParcelableArrayList("weatherElementArray", weatherElementArray);
                        bundle.putString("locationName", locationName);
                        Log.i(MainActivity.TAG, "Model, queryWeatherForecast, apply");
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
                        queryFavoriteList();
                        emitter.onNext(queryFavoriteList());
                        emitter.onComplete();
                        Log.i(MainActivity.TAG, "Model, queryFavoriteList, subscribe");
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
                        SpotDetail spotDetail = getSpotDetail(spotName);
                        emitter.onNext(spotDetail);
                        emitter.onComplete();
                        Log.i(MainActivity.TAG, "Model, queryFavoriteSpotDetail, subscribe");
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
                        SQLiteManager.getInstance().delete(spotName);
                        emitter.onNext(queryFavoriteList());
                        emitter.onComplete();
                        Log.i(MainActivity.TAG, "Model, deleteFavoriteSpot, subscribe");
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

    private void setSpotDetailList(ArrayList<SpotDetail> spotDetailList) {
        mSpotDetailList = spotDetailList;
    }

    private ArrayList<SpotDetail> getSpotDetailList() {
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
        SpotDetail spotDetail = new SpotDetail();
        Cursor cursor = SQLiteManager.getInstance().findSpot(spotName);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            spotDetail.setId(cursor.getString(0));
            spotDetail.setName(cursor.getString(1));
            spotDetail.setCategory(cursor.getString(2));
            spotDetail.setAddress(cursor.getString(3));
            spotDetail.setTelephone(cursor.getString(4));
            spotDetail.setLongitude(cursor.getString(5));
            spotDetail.setLatitude(cursor.getString(6));
            spotDetail.setContent(cursor.getString(7));
            cursor.moveToNext();
        }
        cursor.close();
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
