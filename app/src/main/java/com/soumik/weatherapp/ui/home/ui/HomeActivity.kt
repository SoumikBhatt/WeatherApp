package com.soumik.weatherapp.ui.home.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.soumik.weatherapp.app.WeatherApp
import com.soumik.weatherapp.databinding.ActivityHomeBinding
import com.soumik.weatherapp.ui.details.DetailsActivity
import com.soumik.weatherapp.utils.Constants
import com.soumik.weatherapp.utils.GpsUtils
import com.soumik.weatherapp.utils.Status
import com.soumik.weatherapp.utils.showSnackBar
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_REQUEST = 100
    }

    private var isGPSEnabled = false

    @Inject
    lateinit var mHomeViewModel: HomeViewModel

    private lateinit var binding: ActivityHomeBinding

    private val mCityListAdapter : CityListAdapter by lazy {
        CityListAdapter {
            startActivity(Intent(this,DetailsActivity::class.java)
                .putExtra(DetailsActivity.WEATHER_DATA,it))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as WeatherApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // checking GPS status
        GpsUtils(this).turnGPSOn(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                isGPSEnabled = isGPSEnable
            }

        })

    }

    override fun onStart() {
        super.onStart()

        invokeLocationAction()
    }

    override fun onResume() {
        super.onResume()


        setUpObservers()
        setUpRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==Activity.RESULT_OK) {
            if (requestCode == GpsUtils.GPS_REQUEST) {
                isGPSEnabled = true
                invokeLocationAction()
            }
        }
    }

    /**
     * setting up the recycler view with adapter
     * to show the city list
     */
    private fun setUpRecyclerView() {
        binding.rvCityList.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity,RecyclerView.VERTICAL,false)
            setHasFixedSize(true)
            adapter = mCityListAdapter
        }
    }

    /**
     * observing the live data from [HomeViewModel]
     * and updating the UI accordingly
     */
    private fun setUpObservers() {
        mHomeViewModel.apply {

            locationLiveData.observe(this@HomeActivity,{
                when(it.status) {
                    Status.SUCCESS -> {
                        fetchWeatherByCity(it.data?.latitude.toString(),it.data?.longitude.toString(),"50")
                    }
                    Status.ERROR -> {
                        showSnackBar(binding.root,it.message!!)
                    }
                    Status.LOADING -> {}
                }
            })

            isInternetAvailable.observe(this@HomeActivity,{
                if (!it) {
                    binding.progressCircular.visibility = View.GONE
                    showSnackBar(binding.root,Constants.NO_NETWORK_CONNECTION,Snackbar.LENGTH_LONG)
                } else {
                    binding.progressCircular.visibility = View.VISIBLE
                }
            })

            weatherInfo.observe(this@HomeActivity,{
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.apply {
                            progressCircular.visibility = View.GONE
                            rvCityList.visibility = View.VISIBLE
                        }

                        if (it.data?.list!=null && it.data.list!!.isNotEmpty()) {
                            mCityListAdapter.submitList(it.data.list)
                        } else {
                            mCityListAdapter.submitList(emptyList())
                        }
                    }

                    Status.ERROR -> {
                        showSnackBar(binding.root,it.message!!)
                    }

                    Status.LOADING -> {
                        binding.apply {
                            progressCircular.visibility = View.VISIBLE
                            rvCityList.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }

    /**
     * invoking the location action
     * will check
     * [1]. if the [GPS] is enabled or not
     * [2]. if location permission is granted or not
     * [3]. check if the permission is denied previously
     * other wise requests for permission
     */
    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> showSnackBar(binding.root,"Please Enable GPS")

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> requestLocationPermission()

            else -> requestLocationPermission()
        }
    }

    /**
     * requesting permission from user
     * for location
     */
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_REQUEST
        )
    }

    /**
     * fetching the current location of the user
     */
    private fun startLocationUpdate() {
        mHomeViewModel.getCurrentLocation()
    }

    /**
     * checking if the permission is granted
     */
    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    /**
     * requesting for permission if the user denied the permission once before
     */
    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }
}