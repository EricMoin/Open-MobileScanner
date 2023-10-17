package com.mobilescanner.main.feature_login.remote.service

import com.mobilescanner.main.feature_login.remote.model.LoginBody
import com.mobilescanner.main.feature_login.remote.model.Resource
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @POST("/login")
    @Headers(
        "Content-Type:application/json",
        "Accept:application/json"
    )
    fun login(
        @Body body: RequestBody,
    ): Call< Resource<String> >

    @POST("/user")
    @Headers(
        "Content-Type:application/json",
        "Accept:application/json"
    )
    fun register(
        @Body body: RequestBody,
    ): Call< Resource<String> >
}