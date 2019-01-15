package com.example.eric.newtraveler.retrofit;

import com.example.eric.newtraveler.parcelable.SpotDetail;
import com.example.eric.newtraveler.parcelable.TravelCountyAndCity;

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

    @GET("travelspot/query_spot/")
    Call<ArrayList<SpotDetail>> getSpotDetail();
}
