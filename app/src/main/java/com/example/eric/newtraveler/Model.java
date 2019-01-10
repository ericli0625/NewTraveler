package com.example.eric.newtraveler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.example.eric.newtraveler.presenter.ISubject;
import com.example.eric.newtraveler.view.IObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Model implements ISubject {

    private HandlerThread mBackgroundThread = null;
    private Handler mBackgroundHandler = null;

    private CopyOnWriteArrayList<IObserver> mObserveList = new CopyOnWriteArrayList<IObserver>();

    private String mNowCountyName;
    private String mNowCityName;

    private int mNowCountyPosition;
    private int mNowCityPosition;

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

    public void notifyObservers(String string) {
        for (IObserver iObserver : mObserveList) {
            iObserver.notifyResult(string);
        }
    }

    public void queryAllCountyAndCityList() {
        mBackgroundHandler.post(new RunQueryAllCountyList());
    }

    public void queryCountyList() {
        mBackgroundHandler.post(new RunQueryCountyList());
    }

    public void queryCityList(String cityList, int position) {
        mBackgroundHandler.post(new RunnableQueryCityList(cityList, position));
    }

    public void queryNormalSearchSpot(String countyList, int position) {
        mBackgroundHandler.post(new RunnableQuerySpotList(countyList, position));
    }

    public void queryKeywordSearchSpot(String queryString) {
        mBackgroundHandler.post(new RunnableQueryKeywordSearchSpot(queryString));
    }

    public void QueryWeatherForecast(String countyName) {
        mBackgroundHandler.post(new RunnableQueryWeatherForecast(countyName));
    }

    public class RunQueryCountyList implements Runnable {

        @Override
        public void run() {
            queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelcity/all_county/");
        }

    }

    private class RunQueryAllCountyList implements Runnable {

        @Override
        public void run() {
            queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelcity/");
        }

    }

    public String getListItem(String cityList, int position) {
        String result = null;
        try {
            JSONArray jsonArray = new JSONArray(cityList);
            result = jsonArray.getString(position);
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "Model, getSpotDetailBundle, JSONException");
        }
        return result;
    }

    private class RunnableQueryCityList implements Runnable {

        private String cityList;
        private int position;

        public RunnableQueryCityList(String cityList, int position) {
            this.cityList = cityList;
            this.position = position;
        }

        @Override
        public void run() {
            String queryString = getListItem(cityList, position);
            queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelcity/query_city/?county=" + queryString);
        }
    }

    private class RunnableQuerySpotList implements Runnable {


        private String countyList;
        private int position;

        public RunnableQuerySpotList(String countyList, int position) {
            this.countyList = countyList;
            this.position = position;
        }

        @Override
        public void run() {
            String queryString = getListItem(countyList, position);
            queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelspot/query_spot/?place=" + getNowCountyName()
                    + "," + queryString);
        }
    }

    public class RunnableQueryKeywordSearchSpot implements Runnable {

        private final String queryString;

        public RunnableQueryKeywordSearchSpot(String queryString) {
            this.queryString = queryString;
        }

        @Override
        public void run() {
            queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelspot/query_spot_name/?spot_name=" + queryString);
        }

    }

    private class RunnableQueryWeatherForecast implements Runnable {
        private final String countyName;

        public RunnableQueryWeatherForecast(String countyName) {
            this.countyName = countyName;
        }

        @Override
        public void run() {
            String authorization = "Authorization=CWB-38A07514-8234-4044-AC3D-17FE6A4320BF";
            queryRestFullAPI("GET", "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?locationName=" + countyName + "&" + authorization);
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

    public String getCountyName(String countyList, int position) {
        return getListItem(countyList, position);
    }

    private void queryRestFullAPI(String requestMethod, String url) {
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

        notifyObservers(result);
    }

    public Bundle getSpotDetailBundle(String string, int position) {
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
            return bundle;
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "Model, getSpotDetailBundle, JSONException");
        }
        return null;
    }

    public Bundle getWeatherElement(String jsonWeatherInfoArray) {
        Bundle bundle = new Bundle();
        try {
            JSONObject locationJsonObject = new JSONObject(jsonWeatherInfoArray).getJSONObject("records").getJSONArray("location").getJSONObject(0);
            JSONArray array = locationJsonObject.getJSONArray("weatherElement");
            bundle.putString("weatherElement", array.toString());
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "Model, getWeatherInfo, JSONException");
        }
        return bundle;
    }

}
