package com.soumik.weatherapp.ui.home.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.soumik.weatherapp.ui.home.data.models.LocationData
import com.soumik.weatherapp.utils.RequestCompleteListener
import javax.inject.Inject

//
// Created by Soumik on 11/9/2021.
// piyal.developer@gmail.com
//

class LocationRepository @Inject constructor(var context: Context) {
    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getUserCurrentLocation(callback: RequestCompleteListener<LocationData>) {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            it.also {
                callback.onRequestCompleted(setLocationData(it))
            }
        }.addOnFailureListener {
            callback.onRequestFailed(it.localizedMessage)
        }

        startLocationUpdates()
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun setLocationData(location: Location): LocationData {
        return LocationData(longitude = location.longitude, latitude = location.latitude)
    }
}