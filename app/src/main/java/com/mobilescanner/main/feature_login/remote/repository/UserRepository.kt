package com.mobilescanner.main.feature_login.remote.repository
import com.mobilescanner.main.feature_login.remote.model.LoginBody
import com.mobilescanner.main.feature_login.remote.model.RegisterBody
import com.mobilescanner.main.feature_login.remote.model.toRequestBody
import com.mobilescanner.main.feature_login.remote.network.UserNetWork
import com.mobilescanner.main.main.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.RequestBody

object UserRepository:BaseRepository() {
    fun login( loginBody: LoginBody ) = fire(Dispatchers.IO){
        val body = UserNetWork.login(loginBody.toRequestBody())
        if (body.msg == "success"){
            Result.success(body)
        }else{
            Result.failure(RuntimeException(body.data))
        }
    }
    fun register( loginBody: RegisterBody ) = fire(Dispatchers.IO){
        val body = UserNetWork.register(loginBody.toRequestBody())
        if (body.msg == "success"){
            Result.success(body)
        }else{
            Result.failure(RuntimeException(body.data))
        }
    }
}