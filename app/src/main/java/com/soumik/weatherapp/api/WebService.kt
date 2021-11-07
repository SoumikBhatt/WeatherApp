package com.soumik.weatherapp.api

import com.soumik.weatherapp.ui.home.data.models.WeatherByCityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
// Copyright (c) 2021 Soumik Bhattacharjee. All rights reserved.
//

interface WebService {

    @GET ("find")
    suspend fun weatherByCity(
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("cnt") cnt : String,
    ) : Response<WeatherByCityResponse>

}