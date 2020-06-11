package com.intelegencia.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.intelegencia.models.WeatherResponse
import com.intelegencia.repositories.local.WeatherDao
import com.intelegencia.repositories.local.WeatherEntity
import com.intelegencia.repositories.remote.WeatherService
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)

class WeatherRepositoryTest {

    val LATITUDE: String = "18.52"
    val LONGITUDE: String = "73.85"

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val weatherRepository = WeatherRepository(
        Mockito.mock(WeatherService::class.java),
        Mockito.mock(WeatherDao::class.java)
    )

    @Mock
    lateinit var weatherResponse: WeatherResponse

    @Mock
    lateinit var weatherEntity: WeatherEntity

    @Before
    fun setUp() {
        weatherResponse = Mockito.mock(WeatherResponse::class.java)
        weatherEntity = Mockito.mock(WeatherEntity::class.java)
    }

    @Test
    fun check_getWeatherFromRemote() {
        Mockito.`when`(weatherRepository.getWeatherFromRemote(LATITUDE, LONGITUDE))
            .thenReturn(Observable.just(weatherResponse))

        Assert.assertNotNull(weatherRepository.getWeatherFromRemote(LATITUDE, LONGITUDE))
        verify(weatherRepository).getWeatherFromRemote(LATITUDE, LONGITUDE)
    }

    @Test
    fun check_getWeatherFromRoomDB() {
        Mockito.`when`(weatherRepository.getWeatherFromDb())
            .thenReturn(Observable.just(weatherEntity))

        Assert.assertNotNull(weatherRepository.getWeatherFromDb())
        verify(weatherRepository).getWeatherFromDb()
    }
}
