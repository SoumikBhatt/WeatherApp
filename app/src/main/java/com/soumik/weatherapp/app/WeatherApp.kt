package com.soumik.weatherapp.app

import android.app.Application
import com.soumik.weatherapp.di.components.AppComponent
import com.soumik.weatherapp.di.components.DaggerAppComponent

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//

class WeatherApp : Application() {

    val appComponent : AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}