package com.intelegencia.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelegencia.models.WeatherResponse
import com.intelegencia.repositories.WeatherRepository
import com.intelegencia.repositories.local.WeatherEntity
import com.intelegencia.util.ConnectionDetector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    var weatherResult: MutableLiveData<WeatherEntity> = MutableLiveData()
    var weatherError: MutableLiveData<String> = MutableLiveData()
    lateinit var disposableObserverLocal: DisposableObserver<WeatherEntity>
    lateinit var disposableObserverRemote: DisposableObserver<WeatherResponse>

    /**
     * Function to get live data of weather
     */
    fun weatherResult(): LiveData<WeatherEntity> {
        return weatherResult
    }

    /**
     * Function to handle error
     */
    fun weatherError(): LiveData<String> {
        return weatherError
    }

    /**
     * Function to load weather data
     */
    fun loadWeatherData(context: Context, latitude: String, longitude: String) {
        // Check wehether WIFI is enabled or not
        if (ConnectionDetector.isWifiEnabled(context)) {
            loadWeatherDataFromRemote(latitude, longitude)
        }
        loadWeatherDataFromDb()
    }

    /**
     * Function to load weather data from remote
     */
    fun loadWeatherDataFromRemote(latitude: String, longitude: String) {
        disposableObserverRemote = object : DisposableObserver<WeatherResponse>() {
            override fun onComplete() {
            }

            override fun onNext(weather: WeatherResponse) {
                loadWeatherDataFromDb()
            }

            override fun onError(e: Throwable) {
                weatherError.postValue(e.message)
            }
        }

        weatherRepository.getWeatherFromRemote(latitude, longitude)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(2, TimeUnit.HOURS)
            .subscribe(disposableObserverRemote)
    }

    /**
     * Function to load weather data from Room database
     */
    fun loadWeatherDataFromDb() {
        disposableObserverLocal = object : DisposableObserver<WeatherEntity>() {
            override fun onComplete() {
            }

            override fun onNext(weather: WeatherEntity) {
                weatherResult.postValue(weather)
            }

            override fun onError(e: Throwable) {
            }
        }

        weatherRepository.getWeatherFromDb()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableObserverLocal)
    }

    /**
     * Function to dispose observers
     */
    fun disposeElements() {
        if (null != disposableObserverLocal && !disposableObserverLocal.isDisposed) disposableObserverLocal.dispose()
        if (null != disposableObserverRemote && !disposableObserverRemote.isDisposed) disposableObserverRemote.dispose()
    }
}