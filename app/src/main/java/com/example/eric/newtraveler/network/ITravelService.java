package com.example.eric.newtraveler.network;

import com.example.eric.newtraveler.network.responseData.SpotDetail;
import com.example.eric.newtraveler.network.responseData.TravelCountyAndCity;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITravelService {
    @GET("travelcity")
    Observable<ArrayList<TravelCountyAndCity>> getAllCountyAndCityList();

    @GET("travelcity/all_county/")
    Observable<ArrayList> getAllCountyList();

    @GET("travelcity/query_city")
    Observable<ArrayList> getCityList(@Query("county") String county);

    @GET("travelspot/query_spot")
    Observable<ArrayList<SpotDetail>> getNormalSearchSpotDetail(@Query("place") String spotName);

    @GET("travelspot/query_spot_name")
    Observable<ArrayList<SpotDetail>> getKeywordSearchSpotDetail(@Query("spot_name") String spotName);
}
