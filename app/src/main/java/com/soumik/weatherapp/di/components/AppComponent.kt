package com.soumik.weatherapp.di.components

import android.content.Context
import com.soumik.weatherapp.di.modules.NetworkModule
import com.soumik.weatherapp.home.ui.HomeActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : AppComponent
    }

    fun inject (homeActivity: HomeActivity)
}