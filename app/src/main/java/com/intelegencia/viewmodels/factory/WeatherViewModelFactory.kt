package com.intelegencia.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelegencia.viewmodels.WeatherViewModel
import javax.inject.Inject

class WeatherViewModelFactory @Inject constructor(
    private val weatherViewModel: WeatherViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java!!)) {
            return weatherViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}