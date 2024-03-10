package com.cobaltumapps.accuweathertest

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Path

interface WeatherService {
    @GET("/forecasts/v1/daily/1day/{locationRegionCode}")
    fun getWeatherForecast(
        @Path("locationRegionCode") locationRegionCode: String,
        @Query("apikey") apiKey: String,
        @Query("metric") metric: Boolean = true
    ): Call<WeatherResponse>
}