package com.mobilescanner.main.feature_login.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.mobilescanner.main.feature_login.remote.model.LoginBody
import com.mobilescanner.main.feature_login.remote.model.RegisterBody
import com.mobilescanner.main.feature_login.remote.repository.UserRepository

class LoginViewModel : ViewModel() {
    var registerUserInfo = RegisterBody()
    var loginUserInfo = LoginBody()
    var isReady = false
    var jwtToken = ""
    private val _registerLiveData = MutableLiveData<RegisterBody>()
    val registerLiveData get() = _registerLiveData.switchMap { body ->
        UserRepository.register(body)
    }
    private val _loginLiveData = MutableLiveData<LoginBody>()
    val loginLiveData get() = _loginLiveData.switchMap { body ->
        UserRepository.login(body)
    }
    fun register(){
        _registerLiveData.value = registerUserInfo
    }
    fun login(){
        _loginLiveData.value = loginUserInfo
    }
}