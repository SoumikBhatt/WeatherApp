package com.soumik.weatherapp.ui.home.data.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//

data class WeatherByCityResponse(
    @SerializedName("cod")
    var cod: String?,
    @SerializedName("count")
    var count: Int?,
    @SerializedName("list")
    var list: List<Data>?,
    @SerializedName("message")
    var message: String?
)

@Parcelize
data class Data(
    @SerializedName("clouds")
    var clouds: Clouds?,
    @SerializedName("coord")
    var coord: Coord?,
    @SerializedName("dt")
    var dt: Int?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main")
    var main: Main?,
    @SerializedName("name")
    var name: String?,
//    @SerializedName("rain")
//    var rain: Any?,
//    @SerializedName("snow")
//    var snow: Any?,
    @SerializedName("sys")
    var sys: Sys?,
    @SerializedName("weather")
    var weather: List<Weather>?,
    @SerializedName("wind")
    var wind: Wind?
) : Parcelable

@Parcelize
data class Clouds(
    @SerializedName("all")
    var all: Int?
) : Parcelable

@Parcelize
data class Coord(
    @SerializedName("lat")
    var lat: Double?,
    @SerializedName("lon")
    var lon: Double?
) : Parcelable

@Parcelize
data class Main(
    @SerializedName("feels_like")
    var feelsLike: Double?,
    @SerializedName("grnd_level")
    var grndLevel: Int?,
    @SerializedName("humidity")
    var humidity: Int?,
    @SerializedName("pressure")
    var pressure: Int?,
    @SerializedName("sea_level")
    var seaLevel: Int?,
    @SerializedName("temp")
    var temp: Double?,
    @SerializedName("temp_max")
    var tempMax: Double?,
    @SerializedName("temp_min")
    var tempMin: Double?
) : Parcelable

@Parcelize
data class Sys(
    @SerializedName("country")
    var country: String?
) : Parcelable

@Parcelize
data class Weather(
    @SerializedName("description")
    var description: String?,
    @SerializedName("icon")
    var icon: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main")
    var main: String?
) : Parcelable

@Parcelize
data class Wind(
    @SerializedName("deg")
    var deg: Int?,
    @SerializedName("speed")
    var speed: Double?
) : Parcelable