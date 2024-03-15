package com.demo.weatherinfoapp.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.weatherinfoapp.databinding.ActivityMainBinding
import com.demo.weatherinfoapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadWeatherInfo()
        }
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.weatherResponse.observe(this) {
            //Log.d("tttt", "res >${it.data?.currentWeatherData?.windSpeed}")
            when (it) {
                is Resource.Loading -> {
                    Log.d("tttt", "loading..")
                }

                is Resource.Success -> {
                    Log.d("tttt", "res >${it.data?.currentWeatherData?.windSpeed}")
                    it.data?.let {
                        it.currentWeatherData?.let {
                            val formattedTime = it.time.format(
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                            binding.tvCurrentTime.text = "Today at $formattedTime"
                            binding.ivWeatherIcon.setImageResource(it.weatherType.iconRes)
                            binding.tvTemperature.text = "${it.temperatureCelsius}°C"
                            binding.tvStatus.text = it.weatherType.weatherDesc

                            binding.tvHumidityAndWs.text= "Humidity: ${it.humidity.roundToInt()}% | Wind Speed: ${it.windSpeed.roundToInt()}km/h"
                        }
                        val adapter = PerDayWeatherAdapter(
                            itemList = it.weatherDataPerDay.get(0),
                            onClickItem = {

                            })
                        binding.rvPerDayWeather.layoutManager = LinearLayoutManager(this@MainActivity)
                        binding.rvPerDayWeather.adapter = adapter
                    }
                }

                is Resource.Error -> {
                    Log.d("tttt", "error response >${it.errorMessage}")
                }
            }
        }
    }
}