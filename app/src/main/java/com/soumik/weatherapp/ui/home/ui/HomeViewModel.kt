package com.soumik.weatherapp.ui.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soumik.weatherapp.ui.home.data.models.LocationData
import com.soumik.weatherapp.ui.home.data.models.WeatherByCityResponse
import com.soumik.weatherapp.ui.home.data.repository.LocationRepository
import com.soumik.weatherapp.ui.home.data.repository.WeatherRepository
import com.soumik.weatherapp.utils.Connectivity
import com.soumik.weatherapp.utils.Constants
import com.soumik.weatherapp.utils.RequestCompleteListener
import com.soumik.weatherapp.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
// Copyright (c) 2021 Soumik Bhattacharjee. All rights reserved.
//

class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val connectivity: Connectivity
) : ViewModel() {

    // this live data will store the response fetched from the remote data source
    private var _weatherInfo = MutableLiveData<Resource<WeatherByCityResponse>>()
    val weatherInfo: LiveData<Resource<WeatherByCityResponse>> get() = _weatherInfo

    // live data for checking internet connection
    private var _isInternetAvailable = MutableLiveData<Boolean>()
    val isInternetAvailable: LiveData<Boolean> get() = _isInternetAvailable

    // live data for storing location of the user
    private var _locationLiveData = MutableLiveData<Resource<LocationData>>()
    val locationLiveData : LiveData<Resource<LocationData>> get() = _locationLiveData

    /**
     * getting user current location and storing
     * the [LocationData] in a liveData
     * which will be observed from [HomeActivity]
     */
    fun getCurrentLocation() {

        locationRepository.getUserCurrentLocation(object : RequestCompleteListener<LocationData>{
            override fun onRequestCompleted(data: LocationData) {
                _locationLiveData.postValue(Resource.success(data))
            }

            override fun onRequestFailed(errorMessage: String?) {
                _locationLiveData.postValue(Resource.error(errorMessage))
            }

        })
    }

    /**
     * fetching weather info
     * and storing the response in a liveData
     * which will be observed from [HomeActivity]
     */
    fun fetchWeatherByCity(lat: String, lon: String, count: String) {
        _weatherInfo.postValue(Resource.loading())

        if (connectivity.hasInternetConnection()) {
            _isInternetAvailable.value = true

            viewModelScope.launch {
                try {
                    val response = repository.fetchWeatherByCity(lat, lon, count)

                    if (response.isSuccessful && response.code() == 200) {
                        _weatherInfo.value = Resource.success(response.body())
                    } else {
                        try {
                            _weatherInfo.value = Resource.error(response.body()?.message)
                        } catch (e: Exception) {
                            _weatherInfo.value = Resource.error(Constants.GENERIC_ERROR_MESSAGE)
                        }
                    }

                } catch (e: Exception) {
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