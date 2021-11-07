package com.soumik.weatherapp.ui.home.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.soumik.weatherapp.common.CommonAdapter
import com.soumik.weatherapp.databinding.ItemCityListBinding
import com.soumik.weatherapp.ui.home.data.models.Data
import com.soumik.weatherapp.utils.convertKelvinToCelsius

//
// Created by Soumik on 11/8/2021.
// piyal.developer@gmail.com
//

class CityListAdapter(
    private val callback: ((Data) -> Unit)?
) : CommonAdapter<Data, ItemCityListBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.clouds == newItem.clouds
                    && oldItem.dt == newItem.dt
        }

    }
) {
    override fun createBinding(parent: ViewGroup): ItemCityListBinding {
        return ItemCityListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(binding: ItemCityListBinding, item: Data) {
        binding.apply {
            tvCityName.text = item.name
            tvTemp.text = "${item.main?.temp?.convertKelvinToCelsius()}°C"
            tvWeatherDesc.text = item.weather!![0].description

            root.setOnClickListener {
                callback?.invoke(item)
            }
        }
    }
}