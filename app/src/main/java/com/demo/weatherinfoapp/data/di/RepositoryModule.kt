package com.demo.weatherinfoapp.data.di

import com.demo.weatherinfoapp.data.repository.WeatherDataRepository
import com.demo.weatherinfoapp.data.repository.WeatherDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAnalyticsRepository(weatherDataRepositoryImpl: WeatherDataRepositoryImpl): WeatherDataRepository
}