package com.example.forecast_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/weather?")  // Gets endpoint from https://api.openweathermap.org/
    fun getCurrentWeatherData(@Query("q") q: String,             // For city name
                              @Query("units") units: String,     // For units type (metric or imperial...)
                              @Query("APPID") app_id: String):   // For API-key
            Call<WeatherResponse>
}