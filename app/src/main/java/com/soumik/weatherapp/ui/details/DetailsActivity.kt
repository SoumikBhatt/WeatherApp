package com.soumik.weatherapp.ui.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.soumik.weatherapp.R
import com.soumik.weatherapp.databinding.ActivityDetailsBinding
import com.soumik.weatherapp.ui.home.data.models.Data
import com.soumik.weatherapp.utils.Constants
import com.soumik.weatherapp.utils.convertKelvinToCelsius
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val WEATHER_DATA = "WeatherData"
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailsBinding
    private val mWeatherData: Data? by lazy {
        intent!!.getParcelableExtra(WEATHER_DATA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbDetails)
        binding.tbDetails.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initViews()

    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.apply {
            tvWeatherName.text = mWeatherData?.name
            tvWeatherDesc.text = mWeatherData?.weather!![0].description
            tvWeatherHumidity.text = "Humidity: ${mWeatherData?.main?.humidity}"
            tvWeatherWind.text = "Wind Speed: ${mWeatherData?.wind?.speed}"
            tvWeatherMaxTemp.text =
                "Max. Temp: ${mWeatherData?.main?.tempMax?.convertKelvinToCelsius()}°C"
            tvWeatherMinTemp.text =
                "Min. Temp: ${mWeatherData?.main?.tempMin?.convertKelvinToCelsius()}°C"
            tvTemp.text = "${mWeatherData?.main?.temp?.convertKelvinToCelsius()}°C"
            Picasso.get()
                .load("${Constants.ICON_DOWNLOAD_URL}${mWeatherData?.weather!![0].icon}.png")
                .into(binding.ivWeatherDesc)

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we are adding a marker in the selected city coordinates.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val mLatLng = LatLng(mWeatherData?.coord?.lat!!, mWeatherData?.coord?.lon!!)
        mMap.addMarker(MarkerOptions().position(mLatLng).title("${mWeatherData?.name}"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12f))
        mMap
    }
}