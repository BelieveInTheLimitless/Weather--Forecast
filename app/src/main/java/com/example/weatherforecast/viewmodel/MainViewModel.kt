package com.example.weatherforecast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.model.data.DataOrException
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository)
    : ViewModel(){
        suspend fun getWeatherData(city: String, units: String)
        : DataOrException<Weather, Boolean, Exception> {
            return repository.getWeather(cityQuery = city, units = units)
        }
}