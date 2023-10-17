package com.mobilescanner.main.main.data.repository

import androidx.lifecycle.liveData
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository {
    fun <T> fire(context: CoroutineContext, block:suspend () -> Result<T>) = liveData(context) {
        val result = try{
            block()
        }catch (e: Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun <T> local(context: CoroutineContext, block:suspend () -> T ) = liveData(context) {
        emit( block() )
    }
}