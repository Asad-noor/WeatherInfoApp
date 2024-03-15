package com.demo.weatherinfoapp.utils

sealed class Resource<T>(val data: T? = null, val errorMessage: String?= null) {
    class Loading<T>: Resource<T>()
    class Success<T>(data: T? = null): Resource<T>(data = data)
    class Error<T>(errorMessage: String, data: T? = null): Resource<T>(errorMessage = errorMessage, data = data)
}
