package com.soumik.weatherapp.ui.home.ui

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.soumik.weatherapp.R
import com.soumik.weatherapp.app.WeatherApp
import com.soumik.weatherapp.databinding.ActivityHomeBinding
import com.soumik.weatherapp.notification.NotificationReceiver
import com.soumik.weatherapp.notification.NotificationService
import com.soumik.weatherapp.ui.details.DetailsActivity
import com.soumik.weatherapp.utils.*
import java.util.*
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

        setSupportActionBar(binding.tbHome)

        // checking GPS status
        GpsUtils(this).turnGPSOn(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                isGPSEnabled = isGPSEnable
            }

        })

        scheduleNotification()

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
     * [4]. showing user a permission request dialog, if don't ask again selected
     * other wise requests for permission
     */
    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> showSnackBar(binding.root,"Please Enable GPS")

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> requestLocationPermission()

//            !shouldShowRequestPermissionRationale() -> {
//                binding.progressCircular.visibility = View.GONE
//                openSettingsDialog()
//            }

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

    /**
     * prompting user to provide location permission
     */
    private fun openSettingsDialog() {
        val dialog = MaterialAlertDialogBuilder(this).create()
        val view = layoutInflater.inflate(R.layout.dialog_enable_location_permission, null)
        view?.apply {
            findViewById<Button>(R.id.dialog_btn_cancel).setOnClickListener {
                dialog.dismiss()
            }
            findViewById<Button>(R.id.dialog_btn_go_to_settings).setOnClickListener {
                val intent = createAppSettingsIntent()
                startActivity(intent)
                dialog.dismiss()
            }
        }
        dialog.setView(view)
        dialog.show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    /**
     * scheduling notification service at 10 AM
     */
    private fun scheduleNotification() {
        Log.d("TAG", "scheduleNotification: ")
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =Intent(this,NotificationReceiver::class.java).let {
            PendingIntent.getBroadcast(this,0,it,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY,10)
            set(Calendar.MINUTE,0)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            8640000,
            alarmIntent
        )
    }
}