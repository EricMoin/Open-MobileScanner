package com.mobilescanner.main.feature_login.remote.model

import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

open class PostRequestBody
class LoginBody(
    val username:String = "",
    val password:String = ""
):PostRequestBody()
class RegisterBody(
    var image: String = "NULL",
    var name: String = "佚名",
    var password: String = "",
    var phone: Long = 0L,
    var deadline: String = "2099-12-31T18:00:00",
    var externalField: Int = 4,
    var gender: Int = 1,
    var point: Int = 10000,
    var username: String = ""
):PostRequestBody()
fun RegisterBody.toPair() = mutableListOf<Pair<String,String>>(
    Pair("昵称",name),
    Pair("邮箱",""),
    Pair("联系电话",phone.toString())
)
fun PostRequestBody.toRequestBody() = Gson().toJson(this).toRequestBody("application/json".toMediaType() )
