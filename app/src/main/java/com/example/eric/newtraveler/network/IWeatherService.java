package com.example.eric.newtraveler.network;

import com.example.eric.newtraveler.network.responseData.Weather;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface IWeatherService {
    @GET("F-C0032-001")
    Observable<Weather> getWeather(@Query("locationName") String locationName, @Header("Authorization") String authHeader);
}
