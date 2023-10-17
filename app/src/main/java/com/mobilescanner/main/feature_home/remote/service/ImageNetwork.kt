package com.mobilescanner.main.feature_home.remote.service
import com.mobilescanner.main.main.data.service.ServiceCreator
import com.mobilescanner.main.main.data.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ImageNetwork {
    private val imageService = ServiceCreator.create<ImageService>(Constants.IMAGE_BASE_URL)
    suspend fun enhanceImage(api:String,image:String) = imageService.enhanceImage(api,image).await()
    suspend fun scanOcr(api:String,image:String) = imageService.scanOcr(api,image).await()
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(
                object : Callback<T> {
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        val body = response.body()
                        if (body != null) continuation.resume(body)
                        else continuation.resumeWithException(RuntimeException("Response body is null"))
                    }
                    override fun onFailure(call: Call<T>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            )
        }
    }
    }