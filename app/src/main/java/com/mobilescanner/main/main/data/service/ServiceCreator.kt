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

package com.mobilescanner.main.main.data.service

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private val client = OkHttpClient
        .Builder()
        .addInterceptor (
            LoggingInterceptor.Builder()
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("request")
                .response("response")
                .build()
        )
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(1, TimeUnit.MINUTES)
        .build()
    fun <T> create(serviceClass:Class<T>,url:String):T = Retrofit.Builder()
        .baseUrl(url)//设置服务器路径
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())//获取返回的字符串
        .addConverterFactory(GsonConverterFactory.create())//默认Gson转化库
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加回调库，采用RxJava
        .build()
        .create(serviceClass)
    inline fun <reified T> create(url:String):T = create(T::class.java,url)
}