package com.demo.weatherinfoapp.data.repository

import com.demo.weatherinfoapp.data.remote.WeatherDto
import retrofit2.Response

interface WeatherDataRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Response<WeatherDto>
}