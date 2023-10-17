package com.mobilescanner.main.feature_home.remote.service

import com.mobilescanner.main.feature_home.remote.model.ImageResponse
import com.mobilescanner.main.feature_home.remote.model.OcrGeneralBasicResponse
import com.mobilescanner.main.main.data.utils.Constants
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ImageService {
    /**
     * 图像对比度增强
     */
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("image-process/v1/{api}?access_token=${Constants.ACCESS_TOKEN}")
    fun enhanceImage(@Path("api") api:String, @Field("image") image:String ): Call<ImageResponse>

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("ocr/v1/{api}?access_token=${Constants.ACCESS_TOKEN}&detect_direction=true")
    fun scanOcr(@Path("api") api:String, @Field("image") image:String ): Call<OcrGeneralBasicResponse>
}
/*
https://aip.baidubce.com/rest/2.0/image-process/v1/contrast_enhance?access_token=24.118a9fba21b1ae2db37f0e649d9db852.2592000.1696773599.282335-39014469&image=6666
*/