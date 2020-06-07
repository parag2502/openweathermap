package com.intelegencia.views

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.intelegencia.R
import com.intelegencia.repositories.local.WeatherEntity
import com.intelegencia.util.AppConstants.Companion.WEATHER_ICON_URL
import com.intelegencia.util.PermissionUtils
import com.intelegencia.viewmodels.WeatherViewModel
import com.intelegencia.viewmodels.factory.WeatherViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    @Inject
    lateinit var weatherViewModelFactory: WeatherViewModelFactory
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        AndroidInjection.inject(this)

        weatherViewModel = ViewModelProviders.of(this, weatherViewModelFactory).get(
            WeatherViewModel::class.java
        )


        weatherViewModel.weatherResult().observe(this,
            Observer<WeatherEntity> {
                updateUi(it)
            })

        weatherViewModel.weatherError().observe(this, Observer<String> {
            Toast.makeText(this, getString(R.string.error_something_went_wrong), Toast.LENGTH_LONG)
                .show()
        })
    }

    /**
     * Function to update ui when weatherEntity data changed
     */
    private fun updateUi(weatherEntity: WeatherEntity) {
        tv_city_name.text = weatherEntity?.city ?: "NA"
        tv_temperature.text = weatherEntity?.temp + " \u2103" ?: "NA"
        tv_weather_main.text = weatherEntity?.brief ?: "NA"
        tv_weather_description.text = weatherEntity?.desc ?: "NA"
        tv_humidity.text =
            getString(R.string.humidity) + " " + weatherEntity?.humidity + "%" ?: "NA"
        tv_wind_speed.text =
            getString(R.string.wind_speed) + " " + weatherEntity?.wind + "m/s" ?: "NA"
        var iconUrl = WEATHER_ICON_URL + weatherEntity?.icon + ".png";
        Glide.with(this).load(iconUrl).thumbnail(0.1f)
            .dontAnimate().override(90, 90).into(iv_icon)
    }

    /**
     * Function to set up location listener
     */
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        weatherViewModel.loadWeatherData(
                            context,
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            },
            Looper.myLooper()
        )
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        try {
            weatherViewModel.disposeElements()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }
}
