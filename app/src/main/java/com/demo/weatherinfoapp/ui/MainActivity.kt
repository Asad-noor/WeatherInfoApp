package com.demo.weatherinfoapp.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.demo.weatherinfoapp.R
import com.demo.weatherinfoapp.databinding.ActivityMainBinding
import com.demo.weatherinfoapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

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
                }

                is Resource.Error -> {
                    Log.d("tttt", "error response >${it.errorMessage}")
                }
            }
        }
        //viewModel.loadWeatherInfo()
    }
}