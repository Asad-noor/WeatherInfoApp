package com.demo.weatherinfoapp.data.repository

import com.demo.weatherinfoapp.data.model.WeatherData
import com.demo.weatherinfoapp.data.model.WeatherInfo
import com.demo.weatherinfoapp.data.remote.ApiService
import com.demo.weatherinfoapp.data.remote.WeatherDto
import com.demo.weatherinfoapp.data.remote.toWeatherInfo
import retrofit2.Response
import javax.inject.Inject

class WeatherDataRepositoryImpl @Inject constructor(
    private val api: ApiService
): WeatherDataRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): Response<WeatherDto> {
        return api.getWeatherData(lat = lat, long = long)
    }
}