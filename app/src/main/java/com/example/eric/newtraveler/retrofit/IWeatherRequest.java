package com.example.eric.newtraveler.retrofit;

import com.example.eric.newtraveler.parcelable.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IWeatherRequest {
    @GET("F-C0032-001")
    Call<Weather> getWeather(@Query("locationName") String locationName, @Header("Authorization") String authHeader);
}
