/*
 *
 *    @Copyright 2023 EricMoin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mobilescanner.main.feature_home.data.repository

import com.mobilescanner.main.feature_home.data.dao.PlaceDao
import com.mobilescanner.main.main.data.repository.BaseRepository
import com.mobilescanner.main.feature_home.remote.model.Place
import com.mobilescanner.main.feature_home.remote.model.PlaceResponse
import com.mobilescanner.main.feature_home.remote.model.Weather
import com.mobilescanner.main.feature_home.remote.service.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.RuntimeException

object WeatherRepository :BaseRepository(){
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPLace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
    fun searchPlace(query:String) = fire(Dispatchers.IO){
            val placesResponse = SunnyWeatherNetwork.searchPlaces(query)
            if(placesResponse.status == "ok"){
                val places = placesResponse.places
                Result.success<List<Place>>(places)
            }else{
                Result.failure<RuntimeException>(RuntimeException("Response status is ${placesResponse.status}"))
            }
    }
    fun refreshWeather(lng:String,lat:String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealTime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealTime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            }else {
                Result.failure<RuntimeException>(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}\n" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }
}