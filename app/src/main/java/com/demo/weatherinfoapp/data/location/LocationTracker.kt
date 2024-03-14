package com.demo.weatherinfoapp.data.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}