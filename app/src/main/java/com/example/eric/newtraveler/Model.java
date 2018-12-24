package com.example.eric.newtraveler;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.example.eric.newtraveler.view.IObserver;
import com.example.eric.newtraveler.presenter.ISubject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import javax.net.ssl.HttpsURLConnection;

public class Model implements ISubject {

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

    public class RunQueryCityList implements Runnable {

        @Override
        public void run() {
            queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelcity/all_county/");
        }

    }

    public class RunnableQueryKeywordSearchSpot implements Runnable {

        private final String mQueryString;

        public RunnableQueryKeywordSearchSpot(String queryString) {
            this.mQueryString = queryString;
        }

        @Override
        public void run() {
            queryRestFullAPI("GET", "https://travelplanbackend.herokuapp.com/api/travelspot/query_spot_name/?spot_name=" + mQueryString);
        }

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

}
