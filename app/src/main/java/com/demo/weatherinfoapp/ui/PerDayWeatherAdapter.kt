package com.demo.weatherinfoapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.weatherinfoapp.data.model.WeatherData
import com.demo.weatherinfoapp.databinding.ItemDayWeatherBinding
import java.time.format.DateTimeFormatter

class PerDayWeatherAdapter(
    val itemList: List<WeatherData>?,
    val onClickItem: (WeatherData) -> Unit,
): RecyclerView.Adapter<PerDayWeatherAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDayWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(weatherInfo: WeatherData?) {
            binding.apply {
                weatherInfo?.let {
                    val formattedTime = it.time.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
                    binding.tvTime.text = formattedTime

                    binding.ivWeatherIcon.setImageResource(it.weatherType.iconRes)
                    binding.tvTemperature.text = "${it.temperatureCelsius}°C"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDayWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.bindItems(item)
    }
}