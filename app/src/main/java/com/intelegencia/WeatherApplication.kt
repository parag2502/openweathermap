package com.intelegencia

import android.app.Activity
import android.app.Application
import com.intelegencia.di.component.DaggerAppComponent
import com.intelegencia.di.modules.AppModule
import com.intelegencia.di.modules.WeatherModule
import com.intelegencia.util.AppConstants
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class WeatherApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .weatherModule(WeatherModule(AppConstants.WEATHER_BASE_URL))
            .build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}