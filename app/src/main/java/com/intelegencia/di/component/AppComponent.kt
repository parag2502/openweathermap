package com.intelegencia.di.component

import com.intelegencia.WeatherApplication
import com.intelegencia.di.modules.AppModule
import com.intelegencia.di.modules.BuildersModule
import com.intelegencia.di.modules.WeatherModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AndroidInjectionModule::class, BuildersModule::class, AppModule::class,
        WeatherModule::class
    )
)
interface AppComponent {
    fun inject(app: WeatherApplication)
}