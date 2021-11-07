package com.soumik.weatherapp.home.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.soumik.weatherapp.R
import com.soumik.weatherapp.app.WeatherApp
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var mHomeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as WeatherApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_home)

        mHomeViewModel.testLog()
    }
}