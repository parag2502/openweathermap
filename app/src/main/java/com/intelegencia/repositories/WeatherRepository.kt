package com.intelegencia.repositories

import com.intelegencia.models.WeatherResponse
import com.intelegencia.repositories.local.WeatherDao
import com.intelegencia.repositories.local.WeatherEntity
import com.intelegencia.repositories.remote.WeatherService
import io.reactivex.Observable
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    val weatherService: WeatherService,
    val weatherDao: WeatherDao
) {

    /**
     * This function is used to get weather data from remote
     */
    fun getWeatherFromRemote(lat: String, lon: String): Observable<WeatherResponse> {
        return weatherService.getWeatherData(lat, lon)
            .doOnNext {
                val weatherEntity: WeatherEntity = WeatherEntity(
                    it.id,
                    it.weather[0].main,
                    it.weather[0].description,
                    it.main.temp.toString(),
                    it.name,
                    it.weather[0].icon,
                    it.main.humidity.toString(),
                    it.wind.speed.toString()
                )
                weatherDao.clearData()
                weatherDao.insertWeatherData(weatherEntity)
            }
    }

    /**
     * This function is used to get the data from room database
     */
    fun getWeatherFromDb(): Observable<WeatherEntity> {
        return weatherDao.queryWeatherData()
            .toObservable()
            .doOnNext {
            }
    }
}