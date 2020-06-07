package com.intelegencia.di.modules

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.intelegencia.repositories.local.WeatherDao
import com.intelegencia.repositories.local.WeatherRoomDatabase
import com.intelegencia.util.AppConstants
import com.intelegencia.viewmodels.factory.WeatherViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideWeatherDatabase(app: Application): WeatherRoomDatabase = Room.databaseBuilder(
        app,
        WeatherRoomDatabase::class.java, AppConstants.DB_NAME
    )
        .build()

    @Provides
    @Singleton
    fun provideWeatherDao(
        database: WeatherRoomDatabase
    ): WeatherDao = database.weatherDao()

    @Provides
    fun provideWeatherViewModelFactory(
        factory: WeatherViewModelFactory
    ): ViewModelProvider.Factory = factory
}