package com.soumik.weatherapp.notification

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.soumik.weatherapp.api.WebService
import com.soumik.weatherapp.app.WeatherApp
import com.soumik.weatherapp.ui.home.data.models.LocationData
import com.soumik.weatherapp.ui.home.data.repository.LocationRepository
import com.soumik.weatherapp.utils.Constants
import com.soumik.weatherapp.utils.RequestCompleteListener
import com.soumik.weatherapp.utils.convertKelvinToCelsius
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Service for triggering the notification
 * retrieve users current lat, lon first
 * and then calls the api for fetching weather info
 */
class NotificationService : IntentService("NotificationService") {

    companion object {
        private const val TAG = "NotificationService"
        private const val CHANNEL_ID = "Weather"
    }

    @Inject
    lateinit var webService: WebService

    private lateinit var locationRepository: LocationRepository
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var temperature: Double? = null
    private var icon: String? = null

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()

        (this.application as WeatherApp).appComponent.inject(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {

                val channel = NotificationChannel(CHANNEL_ID, "Weather info", NotificationManager.IMPORTANCE_DEFAULT)

                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build()

                startForeground(1, notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent: ")
        locationRepository = LocationRepository(this)

        fetchCurrentLocation()

    }

    private fun showNotification() {
        Log.d(TAG, "showNotification: Temp: $temperature")
        val notificationUtils = NotificationUtils(this)
        val builder = notificationUtils.setNotification(
            "WeatherAPP",
            "Current Temperature: ${temperature?.convertKelvinToCelsius()}°C",
            "${Constants.ICON_DOWNLOAD_URL}${icon}.png"
        )
        notificationUtils.manager?.notify(999, builder.build())
    }

    private fun fetchCurrentLocation() {
        locationRepository.getUserCurrentLocation(object : RequestCompleteListener<LocationData> {
            override fun onRequestCompleted(data: LocationData) {
                Log.d(TAG, "onRequestCompleted: Lat: ${data.latitude}")
                latitude = data.latitude
                longitude = data.longitude

                ioScope.launch {
                    fetchWeatherByLocation(latitude, longitude)
                }

            }

            override fun onRequestFailed(errorMessage: String?) {
                Log.e(TAG, "onRequestFailed: Location fetching failed: $errorMessage")
            }

        })
    }

    private suspend fun fetchWeatherByLocation(latitude: Double?, longitude: Double?) {

        withContext(Dispatchers.IO) {
            try {
                val response =
                    webService.weatherByLocation(latitude.toString(), longitude.toString())

                if (response.isSuccessful && response.code() == 200) {
                    Log.d(TAG, "fetchWeatherByLocation: Success: ${response.body()?.name}")
                    val weatherInfo = response.body()

                    temperature = weatherInfo?.main?.temp
                    icon = weatherInfo?.weather!![0].icon

                    Log.d(
                        TAG,
                        "fetchWeatherByLocation: Temp: ${weatherInfo.main?.temp?.convertKelvinToCelsius()} .. Condition: ${weatherInfo.weather[0].description}"
                    )

                    showNotification()

                } else {
                    Log.d(TAG, "fetchWeatherByLocation: Unsuccessful: ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "fetchWeatherByLocation: Exception: ${e.localizedMessage}")
            }
        }
    }
}