package com.soumik.weatherapp.di.modules

import com.soumik.weatherapp.api.QueryParameterInterceptor
import com.soumik.weatherapp.api.WebService
import com.soumik.weatherapp.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideWebService(retrofit: Retrofit): WebService {
        return retrofit.create(WebService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(QueryParameterInterceptor())
            .build()
    }

}