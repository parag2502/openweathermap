package com.intelegencia.repositories.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weatherdata")
    fun queryWeatherData(): Single<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(weather: WeatherEntity)

    @Query("DELETE FROM weatherdata")
    fun clearData();
}