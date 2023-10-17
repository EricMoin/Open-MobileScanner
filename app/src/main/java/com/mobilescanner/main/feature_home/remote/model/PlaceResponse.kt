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

package com.mobilescanner.main.feature_home.remote.model
import com.google.gson.annotations.SerializedName


data class PlaceResponse(val status: String,val places:List<Place>)
data class Place(val name:String, val location: Location,
                 @SerializedName("formatted_address") val address:String)
data class Location(val lng:String,val lat:String)