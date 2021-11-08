package com.soumik.weatherapp.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//


/**
 * converting kelvin value to celsius
 * [Formula: KelvinValue − 273.15 = 25.99°C]
 */
fun Double.convertKelvinToCelsius(): Double {
    return String.format("%.2f", this.minus(273.15)).toDouble()
}

fun showSnackBar(root: View, message: String) {
    Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()

}
