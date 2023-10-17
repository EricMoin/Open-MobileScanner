package com.mobilescanner.main.feature_login.remote.model

data class Resource<T>(val code:Int,val msg:String,val data:T?=null)
//{
//    class Success<T>(code:Int,msg:String,data: T):Resource<T>(code,msg,data)
//    class Error<T>(code:Int,msg:String,data: T? = null) : Resource<T>(code,msg,data)
//}