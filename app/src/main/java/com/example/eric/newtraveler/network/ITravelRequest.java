package com.example.eric.newtraveler.network;

import com.example.eric.newtraveler.network.responseData.SpotDetail;
import com.example.eric.newtraveler.network.responseData.TravelCountyAndCity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITravelRequest {
    @GET("travelcity")
    Call<ArrayList<TravelCountyAndCity>> getAllCountyAndCityList();

    @GET("travelcity/all_county/")
    Call<ArrayList> getAllCountyList();

    @GET("travelcity/query_city")
    Call<ArrayList> getCityList(@Query("county") String county);

    @GET("travelspot/query_spot")
    Call<ArrayList<SpotDetail>> getNormalSearchSpotDetail(@Query("place") String spotName);

    @GET("travelspot/query_spot_name")
    Call<ArrayList<SpotDetail>> getKeywordSearchSpotDetail(@Query("spot_name") String spotName);

    @GET("travelspot/query_spot_name")
    Call<ArrayList<SpotDetail>> getTargetSpotDetail(@Query("spot_name") String spotName);
}
