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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Model implements ISubject {

    private final static String TAG_ALL_COUNTY = "all_county";
    private final static String TAG_QUERY_SPOT_NAME = "query_spot_name";
    private final static String TAG_QUERY_COUNTY = "query_county";

    private HandlerThread mBackgroundThread = null;
    private Handler mBackgroundHandler = null;

    private ArrayList<IObserver> mObserveList = new ArrayList<IObserver>();

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

    public void queryCityList() {
        mBackgroundHandler.post(new RunQueryCityList());
    }

    public void queryKeywordSearchSpot(String queryString) {
        mBackgroundHandler.post(new RunnableQueryKeywordSearchSpot(queryString));
    }

    public void queryCountyList(String cityList, int position) {
        mBackgroundHandler.post(new RunnableQueryCountyList(cityList, position));
    }

    public class RunQueryCityList implements Runnable {

        @Override
        public void run() {
            queryRestFullAPI("GET", TAG_ALL_COUNTY, "https://travelplanbackend.herokuapp.com/api/travelcity/all_county/");
        }

    }

    public class RunnableQueryKeywordSearchSpot implements Runnable {

        private final String mQueryString;

        public RunnableQueryKeywordSearchSpot(String queryString) {
            this.mQueryString = queryString;
        }

        @Override
        public void run() {
            queryRestFullAPI("GET", TAG_QUERY_SPOT_NAME, "https://travelplanbackend.herokuapp.com/api/travelspot/query_spot_name/?spot_name=" + mQueryString);
        }

    }

    private class RunnableQueryCountyList implements Runnable {

        private String cityList;
        private int position;

        public RunnableQueryCountyList(String cityList, int position) {
            this.cityList = cityList;
            this.position = position;
        }

        @Override
        public void run() {
            String queryString = null;
            try {
                JSONArray jsonArray = new JSONArray(cityList);
                queryString = jsonArray.getString(position);
            } catch (JSONException e) {
                Log.e(MainActivity.TAG, "Model, getSpotDetailBundle, JSONException");
            }

            queryRestFullAPI("GET", TAG_QUERY_COUNTY, "https://travelplanbackend.herokuapp.com/api/travelcity/query_county/?county=" + queryString);
        }
    }

    private void queryRestFullAPI(String requestMethod, String tag, String url) {
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

}
