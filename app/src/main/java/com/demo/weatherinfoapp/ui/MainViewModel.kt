package com.demo.weatherinfoapp.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.weatherinfoapp.data.location.LocationTracker
import com.demo.weatherinfoapp.data.model.WeatherInfo
import com.demo.weatherinfoapp.data.remote.toWeatherInfo
import com.demo.weatherinfoapp.data.repository.WeatherDataRepository
import com.demo.weatherinfoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherDataRepository,
    private val locationTracker: LocationTracker
): ViewModel() {

    private val _weatherResponse = MutableLiveData<Resource<WeatherInfo>>()
    val weatherResponse: LiveData<Resource<WeatherInfo>>
        get() = _weatherResponse

    fun loadWeatherInfo() {
        viewModelScope.launch {
            _weatherResponse.value = Resource.Loading()

            locationTracker.getCurrentLocation()?.let { location ->
                try {
                    val result = repository.getWeatherData(location.latitude, location.longitude)
                    if (result.body() != null) {
                        _weatherResponse.postValue(Resource.Success(result.body()?.toWeatherInfo()))
                    } else {
                        if (result.errorBody() != null) {
                            Log.d("tttt", "error code " + result.code())
                            _weatherResponse.postValue(Resource.Error("Error"))
                        } else {
                            _weatherResponse.postValue(Resource.Error("Api Error"))
                        }
                    }
                } catch (e: Exception) {
                    val error = handleHttpException(e)
                    _weatherResponse.postValue(Resource.Error(error))
                }

            } ?: kotlin.run {
                _weatherResponse.value = Resource.Error("Couldn't retrieve location. Make sure to grant permission and enable GPS.")
            }
        }
    }

    fun getWeatherByLocation(location: Location) {
        viewModelScope.launch {
            _weatherResponse.value = Resource.Loading()

            try {
                val result = repository.getWeatherData(location.latitude, location.longitude)
                if (result.body() != null) {
                    _weatherResponse.postValue(Resource.Success(result.body()?.toWeatherInfo()))
                } else {
                    if (result.errorBody() != null) {
                        Log.d("tttt", "error code " + result.code())
                        _weatherResponse.postValue(Resource.Error("Error"))
                    } else {
                        _weatherResponse.postValue(Resource.Error("Api Error"))
                    }
                }
            } catch (e: Exception) {
                val error = handleHttpException(e)
                _weatherResponse.postValue(Resource.Error(error))
            }
        }
    }

    private fun handleHttpException(exception: Exception): String {
        var errorStr = ""
        when (exception) {
            is ConnectException -> {
                // Handle connection-related exception
                errorStr = "Internet connection error"
                Log.e("tttt", "Internet connection error: ${exception.message}")
            }

            is SocketTimeoutException -> {
                // Handle socket timeout exception
                errorStr = "Socket timeout error"
                Log.e("tttt", "Socket timeout: ${exception.message}")
            }

            is UnknownHostException -> {
                // Handle unknown host exception (no internet)
                errorStr = "No internet!"
                Log.e("tttt", "No internet connection: ${exception.message}")
            }

            is RuntimeException -> {
                // Handle other runtime exceptions
                errorStr = "Runtime exception occurred"
                Log.e("tttt", "Runtime exception occurred: ${exception.message}")
            }

            else -> {
                // Handle other types of exceptions
                errorStr = "An unexpected exception occurred"
                Log.e("tttt", "An unexpected exception occurred: ${exception.message}")
            }
        }
        if (errorStr.isEmpty()) {
            errorStr = exception.message.toString()
        }
        return errorStr
    }
}