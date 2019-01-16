package com.example.eric.newtraveler;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.newtraveler.database.SQLiteManager;
import com.example.eric.newtraveler.observer.IObserver;
import com.example.eric.newtraveler.observer.ISubject;
import com.example.eric.newtraveler.parcelable.SpotDetail;
import com.example.eric.newtraveler.parcelable.TravelCountyAndCity;
import com.example.eric.newtraveler.parcelable.Weather;
import com.example.eric.newtraveler.retrofit.ITravelRequest;
import com.example.eric.newtraveler.retrofit.IWeatherRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model implements ISubject {

    private static final boolean mUseTempDataToSpeedUp = true;

    private HandlerThread mBackgroundThread = null;
    private Handler mBackgroundHandler = null;

    private final Repository mRepository;

    private CopyOnWriteArrayList<IObserver> mObserveList = new CopyOnWriteArrayList<IObserver>();

    private String mNowCountyName;
    private String mNowCityName;

    private ArrayList<SpotDetail> mSpotDetailList;

    public Model(Repository repository) {
        this.mRepository = repository;
    }

    public void initBackgroundHandler() {
        if (mBackgroundThread == null || !mBackgroundThread.isAlive()) {
            mBackgroundThread = new HandlerThread("background_thread");
            mBackgroundThread.start();
            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        }
    }

    public void addObserver(IObserver observer) {
        mObserveList.add(observer);
    }

    public void removeObserver(IObserver observer) {
        mObserveList.remove(observer);
    }

    @Override
    public <T> void notifyObservers(T string) {
        for (IObserver iObserver : mObserveList) {
            iObserver.notifyResult(string);
        }
    }

    @NonNull
    private ITravelRequest getRetrofitTravelRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://travelplanbackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ITravelRequest.class);
    }

    @NonNull
    private IWeatherRequest getRetrofitWeatherRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opendata.cwb.gov.tw/api/v1/rest/datastore/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(IWeatherRequest.class);
    }

    public void queryAllCountyAndCityList() {
        if (mRepository.isExistPreloadList()) {
            queryCountyList();
        } else {
            ITravelRequest travelRequest = getRetrofitTravelRequest();
            Call<ArrayList<TravelCountyAndCity>> call = travelRequest.getAllCountyAndCityList();
            call.enqueue(mAllCountyListCallback);
        }
    }

    public void backToCityListPage() {
        String countyName = getNowCountyName();
        if (mRepository.isExistPreloadList()) {
            ArrayList repoCityList = mRepository.getCityList(countyName);
            notifyObservers(repoCityList);
        } else {
            queryCityList(countyName);
        }
    }

    public void queryCountyList() {
        if (mRepository.isExistPreloadList()) {
            ArrayList repoCountyList = mRepository.getCountyList();
            notifyObservers(repoCountyList);
        } else {
            ITravelRequest travelRequest = getRetrofitTravelRequest();
            Call<ArrayList> call = travelRequest.getAllCountyList();
            call.enqueue(mCountyListCallback);
        }
    }

    public void queryWeatherCountyList() {
        if (mRepository.isExistPreloadList()) {
            ArrayList repoCountyList = mRepository.getCountyList();
            notifyObservers(repoCountyList);
        } else {
            queryCountyList();
        }
    }

    public void queryCityList(String countyName) {
        setNowCountyStatus(countyName);
        if (mRepository.isExistPreloadList()) {
            ArrayList repoCityList = mRepository.getCityList(countyName);
            notifyObservers(repoCityList);
        } else {
            ITravelRequest travelRequest = getRetrofitTravelRequest();
            Call<ArrayList> call = travelRequest.getCityList(countyName);
            call.enqueue(mCityListCallback);
        }
    }

    public void queryNormalSearchSpot(String cityName) {
        setNowCityStatus(cityName);
        String countyName = getNowCountyName();

        ITravelRequest travelRequest = getRetrofitTravelRequest();
        Call<ArrayList<SpotDetail>> call = travelRequest.getNormalSearchSpotDetail(countyName + "," + cityName);
        call.enqueue(mSpotListCallback);
    }

    public void queryKeywordSearchSpot(String queryString) {
        ITravelRequest travelRequest = getRetrofitTravelRequest();
        Call<ArrayList<SpotDetail>> call = travelRequest.getKeywordSearchSpotDetail(queryString);
        call.enqueue(mSpotListCallback);
    }

    public void querySpotDetail(String spotName) {
        if (mUseTempDataToSpeedUp) {
            mBackgroundHandler.post(new RunnableQuerySpotDetail(spotName, mSpotDetailList));
        } else {
            ITravelRequest travelRequest = getRetrofitTravelRequest();
            Call<ArrayList<SpotDetail>> call = travelRequest.getTargetSpotDetail(spotName);
            call.enqueue(mSpotDetailCallback);
        }
    }

    public void QueryWeatherForecast(String countyName) {
        IWeatherRequest weatherRequest = getRetrofitWeatherRequest();
        Call<Weather> call = weatherRequest.getWeather(countyName, "CWB-38A07514-8234-4044-AC3D-17FE6A4320BF");
        call.enqueue(mWeatherCallback);
    }

    public void QueryFavoriteList() {
        mBackgroundHandler.post(new RunnableQueryFavoriteList());
    }

    public void QueryFavoriteSpotDetail(String spotName) {
        mBackgroundHandler.post(new RunnableQueryFavoriteSpotDetail(spotName));
    }

    public void DeleteFavoriteSpot(String spotName) {
        mBackgroundHandler.post(new RunnableDeleteFavoriteSpot(spotName));
    }

    private Callback<ArrayList<TravelCountyAndCity>> mAllCountyListCallback = new Callback<ArrayList<TravelCountyAndCity>>() {
        private ArrayList repoCityList;

        @Override
        public void onResponse(Call<ArrayList<TravelCountyAndCity>> call, Response<ArrayList<TravelCountyAndCity>> response) {
            if (response.body() != null) {
                mRepository.parserAllCountyAndCityList(response.body());
                repoCityList = mRepository.getCountyList();
            }
            notifyObservers(repoCityList);
        }

        @Override
        public void onFailure(Call<ArrayList<TravelCountyAndCity>> call, Throwable t) {
            Log.e(MainActivity.TAG, "AllCountyListCallback, onFailure");
        }
    };

    private Callback<ArrayList> mCountyListCallback = new Callback<ArrayList>() {

        @Override
        public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
            notifyObservers(response.body());
        }

        @Override
        public void onFailure(Call<ArrayList> call, Throwable t) {
            Log.e(MainActivity.TAG, "CountyListCallback, onFailure");
        }
    };

    private Callback<ArrayList> mCityListCallback = new Callback<ArrayList>() {

        @Override
        public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
            notifyObservers(response.body());
        }

        @Override
        public void onFailure(Call<ArrayList> call, Throwable t) {
            Log.e(MainActivity.TAG, "CityListCallback, onFailure");
        }
    };

    private Callback<ArrayList<SpotDetail>> mSpotListCallback = new Callback<ArrayList<SpotDetail>>() {
        @Override
        public void onResponse(Call<ArrayList<SpotDetail>> call, Response<ArrayList<SpotDetail>> response) {
            ArrayList<String> arrayList = new ArrayList<String>();
            mSpotDetailList = response.body();
            for (SpotDetail spotDetail : mSpotDetailList) {
                arrayList.add(spotDetail.getName());
            }
            notifyObservers(arrayList);
        }

        @Override
        public void onFailure(Call<ArrayList<SpotDetail>> call, Throwable t) {
            Log.e(MainActivity.TAG, "SpotListCallback, onFailure");
        }
    };

    private class RunnableQuerySpotDetail implements Runnable {

        private String spotName;
        private ArrayList<SpotDetail> spotDetailList;

        public RunnableQuerySpotDetail(String spotName, ArrayList<SpotDetail> spotDetailList) {
            this.spotName = spotName;
            this.spotDetailList = spotDetailList;
        }

        @Override
        public void run() {
            notifySpotDetailBundle(spotName, spotDetailList);
        }

        private void notifySpotDetailBundle(String spotName, ArrayList<SpotDetail> list) {
            for (SpotDetail spotDetail : list) {
                if (spotDetail.getName().equals(spotName)) {
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
                    bundle.putBoolean("favorite", false);
                    notifyObservers(bundle);
                }
            }
        }
    }

    private Callback<ArrayList<SpotDetail>> mSpotDetailCallback = new Callback<ArrayList<SpotDetail>>() {
        @Override
        public void onResponse(Call<ArrayList<SpotDetail>> call, Response<ArrayList<SpotDetail>> response) {
            ArrayList<SpotDetail> spotDetail = response.body();

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

            notifyObservers(bundle);
        }

        @Override
        public void onFailure(Call<ArrayList<SpotDetail>> call, Throwable t) {
            Log.e(MainActivity.TAG, "SpotDetailCallback, onFailure");
        }
    };

    private Callback<Weather> mWeatherCallback = new Callback<Weather>() {
        @Override
        public void onResponse(Call<Weather> call, Response<Weather> response) {
            Weather.Location location = null;
            Bundle bundle = new Bundle();
            if (response.body() != null) {
                location = response.body().getRecords().getLocation().get(0);
                ArrayList<Weather.WeatherElement> weatherElementArray = location.getWeatherElement();
                String locationName = location.getLocationName();
                bundle.putParcelableArrayList("weatherElementArray", weatherElementArray);
                bundle.putString("locationName", locationName);
            }
            notifyObservers(bundle);
        }

        @Override
        public void onFailure(Call<Weather> call, Throwable t) {
            Log.e(MainActivity.TAG, "WeatherCallback, onFailure");
        }
    };

    private class RunnableQueryFavoriteList implements Runnable {

        public RunnableQueryFavoriteList() {

        }

        @Override
        public void run() {
            notifyObservers(queryFavoriteList());
        }
    }

    private class RunnableQueryFavoriteSpotDetail implements Runnable {

        private String spotName;

        public RunnableQueryFavoriteSpotDetail(String spotName) {
            this.spotName = spotName;
        }

        @Override
        public void run() {
            queryFavoriteSpotDetail(spotName);
        }

        private void queryFavoriteSpotDetail(String spotName) {
            Bundle bundle = new Bundle();
            Cursor cursor = SQLiteManager.getInstance().findSpot(spotName);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                bundle.putString("id", cursor.getString(0));
                bundle.putString("name", cursor.getString(1));
                bundle.putString("category", cursor.getString(2));
                bundle.putString("address", cursor.getString(3));
                bundle.putString("telephone", cursor.getString(4));
                bundle.putString("longitude", cursor.getString(5));
                bundle.putString("latitude", cursor.getString(6));
                bundle.putString("content", cursor.getString(7));
                cursor.moveToNext();
            }
            cursor.close();

            bundle.putBoolean("favorite", true);

            notifyObservers(bundle);
        }
    }

    private class RunnableDeleteFavoriteSpot implements Runnable {

        private String spotName;

        public RunnableDeleteFavoriteSpot(String spotName) {
            this.spotName = spotName;
        }

        @Override
        public void run() {
            SQLiteManager.getInstance().delete(spotName);
            notifyObservers(queryFavoriteList());
        }
    }

    public String getNowCountyName() {
        return mNowCountyName;
    }

    public void setNowCountyStatus(String countyName) {
        mNowCountyName = countyName;
    }

    public String getNowCityName() {
        return mNowCityName;
    }

    public void setNowCityStatus(String cityName) {
        mNowCityName = cityName;
    }

    @Deprecated
    private String queryRestFullAPI(String requestMethod, String url) {
        URL queryAllCountyUrl = null;
        HttpsURLConnection connection = null;
        BufferedReader bufferedReader = null;
        String result = null;
        try {

            // Create URL1
            queryAllCountyUrl = new URL(url);
            // Create connection
            connection = (HttpsURLConnection) queryAllCountyUrl.openConnection();
            // Request method
            connection.setRequestMethod(requestMethod);
            // Set Content-Type
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            // Connect web api
            connection.connect();

            int statusCode = connection.getResponseCode();

            Log.d(MainActivity.TAG, "Query URL: " + connection.getURL());
            Log.d(MainActivity.TAG, "Query ResponseCode: " + statusCode + ", Message: " + connection.getResponseMessage());

            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Success
                Log.v(MainActivity.TAG, "Query success.");
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                for (; (line = bufferedReader.readLine()) != null; ) {
                    result = line;
//                    Log.d(MainActivity.TAG, "Query result: " + result);
                }
            } else {
                Log.e(MainActivity.TAG, "Query failed, statusCode is not HTTP_OK");
            }
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Query failed, IOException");
        }

        return result;
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

}
