package com.example.eric.newtraveler;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.eric.newtraveler.database.SQLiteManager;
import com.example.eric.newtraveler.observer.ISubject;
import com.example.eric.newtraveler.observer.IObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Model implements ISubject {

    private HandlerThread mBackgroundThread = null;
    private Handler mBackgroundHandler = null;

    private final Repository mRepository;

    private CopyOnWriteArrayList<IObserver> mObserveList = new CopyOnWriteArrayList<IObserver>();

    private String mNowCountyName;
    private String mNowCityName;

    private int mNowCountyPosition;
    private int mNowCityPosition;

    //TODO: refactor them
    private String mFavoriteList;
    private String mSpotList;

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

    public void queryAllCountyAndCityList() {
        if (mRepository.isExistPreloadList()) {
            queryCountyList();
        } else {
            mBackgroundHandler.post(new RunQueryAllCountyList());
        }
    }

    public void backToCityListPage() {
        int position = getNowCountyPosition();
        if (mRepository.isExistPreloadList()) {
            String repoCityList = mRepository.getCityList(position);
            notifyObservers(repoCityList);
        } else {
            queryCityList(position);
        }
    }

    public void queryCountyList() {
        if (mRepository.isExistPreloadList()) {
            String repoCountyList = mRepository.getCountyList();
            notifyObservers(repoCountyList);
        } else {
            mBackgroundHandler.post(new RunQueryCountyList());
        }
    }

    public void queryWeatherCountyList() {
        if (mRepository.isExistPreloadList()) {
            String repoCountyList = mRepository.getCountyList();
            notifyObservers(repoCountyList);
        } else {
            queryCountyList();
        }
    }

    public void queryCityList(int position) {
        String repoCountyList = mRepository.getCountyList();
        setNowCountyStatus(repoCountyList, position);
        if (mRepository.isExistPreloadList()) {
            String repoCityList = mRepository.getCityList(position);
            notifyObservers(repoCityList);
        } else {
            mBackgroundHandler.post(new RunnableQueryCityList(repoCountyList, position));
        }
    }

    public void queryNormalSearchSpot(int position) {
        String repoCityList = mRepository.getCityList(getNowCountyPosition());
        setNowCityStatus(repoCityList, position);
        mBackgroundHandler.post(new RunnableQuerySpotList(repoCityList, position));
    }

    public void queryKeywordSearchSpot(String queryString) {
        mBackgroundHandler.post(new RunnableQueryKeywordSearchSpot(queryString));
    }

    public void querySpotDetail(int position) {
        mBackgroundHandler.post(new RunnableQuerySpotDetail(position));
    }

    public void QueryWeatherForecast(int position) {
        String repoCountyList = mRepository.getCountyList();
        mBackgroundHandler.post(new RunnableQueryWeatherForecast(repoCountyList, position));
    }

    public void QueryFavoriteList() {
        mBackgroundHandler.post(new RunnableQueryFavoriteList());
    }

    public void QueryFavoriteSpotDetail(int position) {
        mBackgroundHandler.post(new RunnableQueryFavoriteSpotDetail(position));
    }

    public void DeleteFavoriteSpot(int position) {
        mBackgroundHandler.post(new RunnableDeleteFavoriteSpot(position));
    }

    public class RunQueryCountyList implements Runnable {

        @Override
        public void run() {
            String result = queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelcity/all_county/");
            notifyObservers(result);
        }

    }

    private class RunQueryAllCountyList implements Runnable {

        @Override
        public void run() {
            String result = queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelcity/");
            mRepository.parserAllCountyAndCityList((String) result);
            notifyObservers(mRepository.getCountyList());
        }

    }

    public String getListItem(String cityList, int position) {
        String result = null;
        try {
            JSONArray jsonArray = new JSONArray(cityList);
            result = jsonArray.getString(position);
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "Model, getListItem, JSONException");
        }
        return result;
    }

    private class RunnableQueryCityList implements Runnable {

        private String queryCounty;

        public RunnableQueryCityList(String repoCountyList, int position) {
            this.queryCounty = getListItem(repoCountyList, position);
        }

        @Override
        public void run() {
            String result = queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelcity/query_city/?county=" + queryCounty);
            notifyObservers(result);
        }
    }

    private class RunnableQuerySpotList implements Runnable {

        private String queryCounty;
        private String queryCity;

        public RunnableQuerySpotList(String repoCityList, int position) {
            this.queryCounty = getNowCountyName();
            this.queryCity = getListItem(repoCityList, position);
        }

        @Override
        public void run() {
            mSpotList = queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelspot/query_spot/?place=" + queryCounty + "," + queryCity);
            notifyObservers(mSpotList);
        }
    }

    public class RunnableQueryKeywordSearchSpot implements Runnable {

        private final String queryString;

        public RunnableQueryKeywordSearchSpot(String queryString) {
            this.queryString = queryString;
        }

        @Override
        public void run() {
            mSpotList = queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelspot/query_spot_name/?spot_name=" + queryString);
            notifyObservers(mSpotList);
        }

    }

    private class RunnableQuerySpotDetail implements Runnable {

        private String result;
        private int position;

        public RunnableQuerySpotDetail(int position) {
            this.result = mSpotList;
            this.position = position;
        }

        @Override
        public void run() {
            Bundle bundle = getSpotDetailBundle(result, position);
            notifyObservers(bundle);
        }

        private Bundle getSpotDetailBundle(String string, int position) {
            try {
                JSONArray jsonArray = new JSONArray(string);
                JSONObject jsonobject = jsonArray.getJSONObject(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", jsonobject.getString("id"));
                bundle.putString("name", jsonobject.getString("name"));
                bundle.putString("city", jsonobject.getString("city"));
                bundle.putString("county", jsonobject.getString("county"));
                bundle.putString("category", jsonobject.getString("category"));
                bundle.putString("address", jsonobject.getString("address"));
                bundle.putString("telephone", jsonobject.getString("telephone"));
                bundle.putString("longitude", jsonobject.getString("longitude"));
                bundle.putString("latitude", jsonobject.getString("latitude"));
                bundle.putString("content", jsonobject.getString("content"));
                bundle.putBoolean("favorite", false);
                return bundle;
            } catch (JSONException e) {
                Log.e(MainActivity.TAG, "Model, getSpotDetailBundle, JSONException");
            }
            return null;
        }

    }

    private class RunnableQueryWeatherForecast implements Runnable {
        private String countyName;

        public RunnableQueryWeatherForecast(String repoCountyList, int position) {
            this.countyName =  getListItem(repoCountyList, position);
        }

        @Override
        public void run() {
            String authorization = "Authorization=CWB-38A07514-8234-4044-AC3D-17FE6A4320BF";
            String result = queryRestFullAPI("GET", "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?locationName=" + countyName + "&" + authorization);
            Bundle bundle = getWeatherElement(result);
            notifyObservers(bundle);
        }

        private Bundle getWeatherElement(String jsonWeatherInfoArray) {
            Bundle bundle = new Bundle();
            try {
                JSONObject locationJsonObject = new JSONObject(jsonWeatherInfoArray).getJSONObject("records").getJSONArray("location").getJSONObject(0);
                JSONArray array = locationJsonObject.getJSONArray("weatherElement");
                String locationName = locationJsonObject.getString("locationName");
                bundle.putString("weatherElement", array.toString());
                bundle.putString("locationName", locationName);
            } catch (JSONException e) {
                Log.e(MainActivity.TAG, "Model, getWeatherElement, JSONException");
            }
            return bundle;
        }
    }

    private class RunnableQueryFavoriteList implements Runnable {

        public RunnableQueryFavoriteList() {

        }

        @Override
        public void run() {
            queryFavoriteList();
        }

    }

    private class RunnableQueryFavoriteSpotDetail implements Runnable {

        private String result;
        private int position;

        public RunnableQueryFavoriteSpotDetail(int position) {
            this.result = mFavoriteList;
            this.position = position;
        }

        @Override
        public void run() {
            queryFavoriteSpotDetail(result, position);
        }

        private void queryFavoriteSpotDetail(String result, int position) {
            String spotName = getFavoriteSpot(result, position);

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

        private String result;
        private int position;

        public RunnableDeleteFavoriteSpot(int position) {
            this.result = mFavoriteList;
            this.position = position;
        }

        @Override
        public void run() {
            String spotName = getFavoriteSpot(result, position);
            SQLiteManager.getInstance().delete(spotName);
            queryFavoriteList();
        }
    }

    public String getNowCountyName() {
        return mNowCountyName;
    }

    public int getNowCountyPosition() {
        return mNowCountyPosition;
    }

    public void setNowCountyStatus(String countyList, int position) {
        mNowCountyName = getListItem(countyList, position);
        mNowCountyPosition = position;
    }

    public String getNowCityName() {
        return mNowCityName;
    }

    public int getNowCityPosition() {
        return mNowCityPosition;
    }

    public void setNowCityStatus(String cityList, int position) {
        mNowCityName = getListItem(cityList, position);
        mNowCityPosition = position;
    }

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

    private String getFavoriteSpot(String result, int position) {
        String spotName = null;
        try {
            JSONArray mJsonArray = new JSONArray(result);
            spotName = (String) mJsonArray.get(position);
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "getFavoriteSpot, JSONException");
        }
        return spotName;
    }

    private void queryFavoriteList() {
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
        JSONArray jsArray = new JSONArray(arrayList);
        mFavoriteList = jsArray.toString();
        notifyObservers(mFavoriteList);
    }

}
