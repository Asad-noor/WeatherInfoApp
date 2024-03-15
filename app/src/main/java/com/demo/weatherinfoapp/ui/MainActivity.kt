package com.demo.weatherinfoapp.ui

import android.Manifest
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.weatherinfoapp.databinding.ActivityMainBinding
import com.demo.weatherinfoapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
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
                    binding.clProgressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.clProgressBar.visibility = View.GONE
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
                    binding.clProgressBar.visibility = View.GONE
                    Log.d("tttt", "error response >${it.errorMessage}")
                }

                else -> {
                    binding.clProgressBar.visibility = View.GONE
                }
            }
        }

        binding.inputCityWeather.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val location = (view as EditText).text.toString()
                if (location.isEmpty()) {

                } else {
                    getLocationFromAddress(location, this@MainActivity)
                }
            }
            false
        }
    }

    private fun getLocationFromAddress(location: String, context: Context) {
        val geocoder = Geocoder(context)
        try {
            val addressList: List<Address>? = geocoder.getFromLocationName(location, 1)
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                val latitude = address.latitude
                val longitude = address.longitude

                val loc = Location("")
                loc.latitude = latitude
                loc.longitude = longitude
                viewModel.getWeatherByLocation(loc)
            } else {
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error retrieving location", Toast.LENGTH_SHORT).show()
        }
    }
}