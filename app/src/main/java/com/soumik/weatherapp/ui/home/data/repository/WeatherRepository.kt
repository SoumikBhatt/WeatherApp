package com.soumik.weatherapp.ui.home.data.repository

import com.soumik.weatherapp.api.WebService
import com.soumik.weatherapp.ui.home.data.models.WeatherByCityResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//

@Singleton
class WeatherRepository @Inject constructor(private val webService: WebService){

    suspend fun fetchWeatherByCity(lat:String,lon:String,count:String) : Response<WeatherByCityResponse> {
        return withContext(Dispatchers.IO) {
            webService.weatherByCity(lat,lon,count)
        }
    }

}