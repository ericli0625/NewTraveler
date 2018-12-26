package com.example.eric.newtraveler;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Repository {

    private final SharedPreferences sharedPreferences;

    private final static String TAG_COUNTY_LIST = "countylist";
    private final static String TAG_CITY_LIST = "citylist";

    public Repository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isExistPreloadList() {
        return !sharedPreferences.getString(TAG_COUNTY_LIST, "").equals("");
    }

    public String getCountyList() {
        return sharedPreferences.getString(TAG_COUNTY_LIST, "");
    }

    public String getCityList(int index) {
        return sharedPreferences.getString(TAG_CITY_LIST + "_" + index, "");
    }

    public void parserAllCountyAndCityList(String string) {
        try {
            ArrayList<String> countyList = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                String county = jsonobject.getString("county");
                if (!countyList.contains(county)) {
                    countyList.add(county);
                }
            }
            sharedPreferences.edit().putString(TAG_COUNTY_LIST, new JSONArray(countyList).toString()).apply();

            ArrayList<ArrayList<String>> cityList = new ArrayList<ArrayList<String>>();
            for (int i = 0; i < countyList.size(); i++) {
                ArrayList<String> innerCityList = new ArrayList<String>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(j);
                    String county = jsonobject.getString("county");
                    if (countyList.get(i).equals(county)) {
                        String city = jsonobject.getString("city");
                        innerCityList.add(city);
                    }
                }
                cityList.add(innerCityList);
                sharedPreferences.edit().putString(TAG_CITY_LIST + "_" + i, new JSONArray(cityList.get(i)).toString()).apply();
            }

        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "parserAllCountyAndCityList, JSONException");
        }
    }

}
