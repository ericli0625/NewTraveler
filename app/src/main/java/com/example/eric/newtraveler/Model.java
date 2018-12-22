package com.example.eric.newtraveler;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.example.eric.newtraveler.presenter.IObserver;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


import javax.net.ssl.HttpsURLConnection;

public class Model {

    private String mResult;
    private HandlerThread mBackgroundThread = null;
    private Handler mBackgroundHandler = null;

    private ArrayList<IObserver> mListArray = new ArrayList<IObserver>();

    public void initBackgroundHandler() {
        if (mBackgroundThread == null || !mBackgroundThread.isAlive()) {
            mBackgroundThread = new HandlerThread("background thread");
            mBackgroundThread.start();

            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        }
    }

    public void addObserver(Presenter.PresenterObserver presenterObserver) {
        mListArray.add(presenterObserver);
    }

    public void removeObserver(Presenter.PresenterObserver presenterObserver) {
        mListArray.remove(presenterObserver);
    }

    public void notifyObservers(String string) {
        for (IObserver iObserver : mListArray) {
            iObserver.notifyResult(string);
        }
    }

    public void handleAllCounty() {
        mBackgroundHandler.post(runHandleAllCounty);
    }

    private Runnable runHandleAllCounty = new Runnable() {

        public void run() {

            URL queryAllCountyUrl = null;
            HttpsURLConnection connection = null;
            BufferedReader bufferedReader = null;
            try {

                // Create URL1
                queryAllCountyUrl = new URL("https://travelplanbackend.herokuapp.com/api/travelcity/all_county/");
                // Create connection
                connection = (HttpsURLConnection) queryAllCountyUrl.openConnection();
                // Request method
                connection.setRequestMethod("GET");
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
                        mResult = line;
                        Log.d(MainActivity.TAG, "Query Result: " + mResult);
                    }
                    notifyObservers(mResult);
                } else {
                    // Error
                    Log.e(MainActivity.TAG, "Query failed.");
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    };

}
