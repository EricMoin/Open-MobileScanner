package com.mobilescanner.main.feature_home.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.mobilescanner.main.feature_home.data.repository.WeatherRepository
import com.mobilescanner.main.feature_home.remote.model.Location
import com.mobilescanner.main.feature_home.remote.model.Place
import com.mobilescanner.main.feature_home.remote.model.Weather

class HomeViewModel:ViewModel() {
    private val _weatherLiveData = MutableLiveData<Location>()
    val weatherLiveData get() = _weatherLiveData.switchMap { location ->
        WeatherRepository.refreshWeather(location.lng,location.lat)
    }
    fun refreshWeather(locationLng: String, locationLat: String) {
        _weatherLiveData.value = Location(locationLng,locationLat)
    }

    fun savePlace(place: Place) = WeatherRepository.savePlace(place)
    fun getSavedPlace() = WeatherRepository.getSavedPLace()
    fun isPlaceSaved() = WeatherRepository.isPlaceSaved()
    var weather:Weather ?= null
    var locationLng: String = ""
    var locationLat: String = ""
}