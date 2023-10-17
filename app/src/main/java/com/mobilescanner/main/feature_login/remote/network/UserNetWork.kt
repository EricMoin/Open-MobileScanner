package com.mobilescanner.main.feature_login.remote.network

import com.mobilescanner.main.feature_login.remote.model.LoginBody
import com.mobilescanner.main.feature_login.remote.network.UserNetWork.await
import com.mobilescanner.main.feature_login.remote.service.UserService
import com.mobilescanner.main.main.data.network.BaseNetWork
import com.mobilescanner.main.main.data.service.ServiceCreator
import com.mobilescanner.main.main.data.utils.Constants
import okhttp3.RequestBody

object UserNetWork :BaseNetWork(){
    private val userService = ServiceCreator.create<UserService>(Constants.USER_BASE_URL)
    suspend fun login(body:RequestBody) = userService.login(body).await()
    suspend fun register(body:RequestBody) = userService.register(body).await()

}