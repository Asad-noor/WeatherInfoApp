package com.demo.weatherinfoapp.data.remote

import com.google.gson.annotations.SerializedName


data class WeatherDto(
    @SerializedName("hourly")
    val weatherData: WeatherDataDto
)
