package com.example.eric.newtraveler;

import android.content.SharedPreferences;

import com.example.eric.newtraveler.parcelable.TravelCountyAndCity;

import org.json.JSONArray;

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

    public String getCityList(String countyName) {
        return sharedPreferences.getString(TAG_CITY_LIST + "_" + countyName, "");
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
