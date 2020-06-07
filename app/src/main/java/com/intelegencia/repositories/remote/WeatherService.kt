package com.intelegencia.repositories.remote

import com.intelegencia.models.WeatherResponse
import com.intelegencia.util.AppConstants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String = AppConstants.WEATHER_API_APP_ID,
        @Query("units") units: String = "metric"

    ): Observable<WeatherResponse>
}