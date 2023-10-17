package com.mobilescanner.main.feature_home.remote.model

import com.google.gson.annotations.SerializedName
data class ImageResponse(
    @SerializedName("log_id") val logId:String,
    val image:String
)

