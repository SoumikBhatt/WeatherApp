package com.soumik.weatherapp.ui.home.data.models
import com.google.gson.annotations.SerializedName


data class WeatherByLocationResponse(
    @SerializedName("base")
    val base: String?,
    @SerializedName("clouds")
    val clouds: CloudsByLocation?,
    @SerializedName("cod")
    val cod: Int?,
    @SerializedName("coord")
    val coord: CoordByLocation?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: MainByLocation?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("sys")
    val sys: SysByLocation?,
    @SerializedName("timezone")
    val timezone: Int?,
    @SerializedName("visibility")
    val visibility: Int?,
    @SerializedName("weather")
    val weather: List<WeatherByLocation>?,
    @SerializedName("wind")
    val wind: WindByLocation?
)

data class CloudsByLocation(
    @SerializedName("all")
    val all: Int?
)

data class CoordByLocation(
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lon")
    val lon: Double?
)

data class MainByLocation(
    @SerializedName("feels_like")
    val feelsLike: Double?,
    @SerializedName("grnd_level")
    val grndLevel: Int?,
    @SerializedName("humidity")
    val humidity: Int?,
    @SerializedName("pressure")
    val pressure: Int?,
    @SerializedName("sea_level")
    val seaLevel: Int?,
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("temp_max")
    val tempMax: Double?,
    @SerializedName("temp_min")
    val tempMin: Double?
)

data class SysByLocation(
    @SerializedName("country")
    val country: String?,
    @SerializedName("sunrise")
    val sunrise: Int?,
    @SerializedName("sunset")
    val sunset: Int?
)

data class WeatherByLocation(
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: String?
)

data class WindByLocation(
    @SerializedName("deg")
    val deg: Int?,
    @SerializedName("gust")
    val gust: Double?,
    @SerializedName("speed")
    val speed: Double?
)