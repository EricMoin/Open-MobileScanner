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

package com.mobilescanner.main.feature_home.data.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.mobilescanner.main.main.ui.MobileScannerApplication
import com.mobilescanner.main.feature_home.remote.model.Place

object PlaceDao {
    private fun sharedPreferences() = MobileScannerApplication.context
        .getSharedPreferences("mobile_scanner_weather", Context.MODE_PRIVATE)
    fun isPlaceSaved () = sharedPreferences().contains("place")
    fun savePlace(place: Place){
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }
    fun getSavedPlace () : Place {
        val placeJson = sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson, Place::class.java)
    }
}