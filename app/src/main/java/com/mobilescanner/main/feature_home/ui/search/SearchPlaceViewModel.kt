package com.mobilescanner.main.feature_home.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.mobilescanner.main.feature_home.data.repository.WeatherRepository
import com.mobilescanner.main.feature_home.remote.model.Place

class SearchPlaceViewModel : ViewModel() {
    private val _placeLiveData = MutableLiveData<String>()
    val placeList = mutableListOf<Place>()
    val placeLiveData = _placeLiveData.switchMap { query ->
        WeatherRepository.searchPlace(query)
    }
    fun searchPlace(query:String){
        _placeLiveData.value = query
    }
    fun savePlace(place: Place) = WeatherRepository.savePlace(place)
}