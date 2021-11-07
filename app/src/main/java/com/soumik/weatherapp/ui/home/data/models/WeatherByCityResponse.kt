package com.soumik.weatherapp.ui.home.data.models
import com.google.gson.annotations.SerializedName


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
    @SerializedName("rain")
    var rain: Any?,
    @SerializedName("snow")
    var snow: Any?,
    @SerializedName("sys")
    var sys: Sys?,
    @SerializedName("weather")
    var weather: List<Weather>?,
    @SerializedName("wind")
    var wind: Wind?
)

data class Clouds(
    @SerializedName("all")
    var all: Int?
)

data class Coord(
    @SerializedName("lat")
    var lat: Double?,
    @SerializedName("lon")
    var lon: Double?
)

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
)

data class Sys(
    @SerializedName("country")
    var country: String?
)

data class Weather(
    @SerializedName("description")
    var description: String?,
    @SerializedName("icon")
    var icon: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main")
    var main: String?
)

data class Wind(
    @SerializedName("deg")
    var deg: Int?,
    @SerializedName("speed")
    var speed: Double?
)