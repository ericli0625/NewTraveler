package com.example.eric.newtraveler.util;

import android.content.SharedPreferences;

import com.example.eric.newtraveler.network.responseData.TravelCountyAndCity;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class Repository {

    private final SharedPreferences sharedPreferences;

    private final static String TAG_COUNTY_LIST = "countylist";
    private final static String TAG_CITY_LIST = "citylist";

    public Repository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isExistPreloadList() {
        return !sharedPreferences.getString(TAG_COUNTY_LIST, "").equals("");
    }

    public ArrayList getCountyList() {
        String string = sharedPreferences.getString(TAG_COUNTY_LIST, "");
        Gson gson = new Gson();
        return gson.fromJson(string, ArrayList.class);
    }

    public ArrayList getCityList(String countyName) {
        String string = sharedPreferences.getString(TAG_CITY_LIST + "_" + countyName, "");
        Gson gson = new Gson();
        return gson.fromJson(string, ArrayList.class);
    }

    public void parserAllCountyAndCityList(ArrayList<TravelCountyAndCity> arrayList) {

        ArrayList<String> countyList = new ArrayList<String>();
        for (int i = 0; i < arrayList.size(); i++) {
            String county = arrayList.get(i).getCounty();
            if (!countyList.contains(county)) {
                countyList.add(county);
            }
        }
        sharedPreferences.edit().putString(TAG_COUNTY_LIST, new JSONArray(countyList).toString()).apply();

        ArrayList<ArrayList<String>> cityList = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < countyList.size(); i++) {
            ArrayList<String> innerCityList = new ArrayList<String>();
            for (int j = 0; j < arrayList.size(); j++) {
                String countyName = arrayList.get(j).getCounty();
                if (countyList.get(i).equals(countyName)) {
                    String cityName = arrayList.get(j).getCity();
                    innerCityList.add(cityName);
                }
            }
            cityList.add(innerCityList);
            sharedPreferences.edit().putString(TAG_CITY_LIST + "_" + countyList.get(i), new JSONArray(cityList.get(i)).toString()).apply();
        }

    }

}
