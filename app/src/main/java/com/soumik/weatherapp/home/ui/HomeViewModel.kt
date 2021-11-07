package com.soumik.weatherapp.home.ui

import androidx.lifecycle.ViewModel
import com.soumik.weatherapp.home.data.repository.WeatherRepository
import javax.inject.Inject

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
// Copyright (c) 2021 Soumik Bhattacharjee. All rights reserved.
//

class HomeViewModel @Inject constructor(val repository: WeatherRepository) : ViewModel() {

    fun testLog() {
        repository.testLog()
    }

}