package com.soumik.weatherapp.ui.home.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soumik.weatherapp.ui.home.data.models.WeatherByCityResponse
import com.soumik.weatherapp.ui.home.data.repository.WeatherRepository
import com.soumik.weatherapp.utils.Connectivity
import com.soumik.weatherapp.utils.Constants
import com.soumik.weatherapp.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
// Copyright (c) 2021 Soumik Bhattacharjee. All rights reserved.
//

class HomeViewModel @Inject constructor(private val repository: WeatherRepository, private val connectivity: Connectivity) : ViewModel() {

    // this live data will store the response fetched from the remote data source
    private var _weatherInfo = MutableLiveData<Resource<WeatherByCityResponse>>()
    val weatherInfo : LiveData<Resource<WeatherByCityResponse>> get() = _weatherInfo

    // live data for checking internet connection
    private var _isInternetAvailable = MutableLiveData<Boolean>()
    val isInternetAvailable : LiveData<Boolean> get() = _isInternetAvailable

    fun testLog() {
        repository.testLog()
    }

    /**
     * fetching weather info
     * and storing the response in a liveData
     * which will be observed from [HomeActivity]
     */
    fun fetchWeatherByCity(lat:String, lon:String, count:String) {
        _weatherInfo.postValue(Resource.loading())

        if (connectivity.hasInternetConnection()) {
            _isInternetAvailable.value = true

            viewModelScope.launch {
                try {
                    val response = repository.fetchWeatherByCity(lat, lon, count)

                    if (response.isSuccessful && response.code()==200) {
                        _weatherInfo.value = Resource.success(response.body())
                    } else _weatherInfo.value = Resource.error(Constants.GENERIC_ERROR_MESSAGE)

                } catch (e:Exception) {
                    Log.e(TAG, "fetchWeatherByCity: Exception: ${e.localizedMessage}")
                    _weatherInfo.value = Resource.error(Constants.GENERIC_ERROR_MESSAGE)
                }
            }

        } else {
            _isInternetAvailable.value = false
        }

    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}