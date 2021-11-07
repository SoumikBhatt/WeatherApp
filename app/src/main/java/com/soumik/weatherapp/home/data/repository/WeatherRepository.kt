package com.soumik.weatherapp.home.data.repository

import android.util.Log
import com.soumik.weatherapp.api.WebService
import javax.inject.Inject
import javax.inject.Singleton

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//

@Singleton
class WeatherRepository @Inject constructor(val webService: WebService){

    companion object {
        private const val TAG = "WeatherRepository"
    }

    fun testLog() {
        Log.d(TAG, "testLog: Success!")
    }

}