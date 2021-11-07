package com.soumik.weatherapp.api

import com.soumik.weatherapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//

class QueryParameterInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("appid", Constants.APP_ID)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }


}