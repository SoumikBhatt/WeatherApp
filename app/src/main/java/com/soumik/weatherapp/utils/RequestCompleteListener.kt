package com.soumik.weatherapp.utils

//
// Created by Soumik on 11/9/2021.
// piyal.developer@gmail.com
//

interface RequestCompleteListener<T> {
    fun onRequestCompleted(data:T)
    fun onRequestFailed(errorMessage:String?)
}