package com.mobilescanner.main.feature_project.data.repository

import com.mobilescanner.main.feature_project.data.entity.ImageItem
import com.mobilescanner.main.main.ui.MobileScannerApplication.Companion.context
import com.mobilescanner.main.feature_home.remote.service.ImageNetwork
import com.mobilescanner.main.feature_project.data.database.ImageItemDatabase
import com.mobilescanner.main.main.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread

object ImageRepository :BaseRepository(){
    private val dao = ImageItemDatabase.getDataBase(context).dao
    fun insertHistory(item: ImageItem) = thread{
        dao.insertHistory(item)
    }
    fun updateHistory(newItem: ImageItem) =  local(Dispatchers.IO){
        dao.updateHistory(newItem)
    }
    fun deleteHistory(item: ImageItem) = thread {
        dao.deleteHistory(item)
    }
    fun deleteHistoryByTitle(title:String) = thread {
        dao.deleteHistoryByTitle(title)
    }
    fun deleteAllHistory() = local(Dispatchers.IO) {
        dao.deleteAllHistory()
    }
    fun loadAllHistory() = local(Dispatchers.IO){
        dao.loadAllHistory()
    }
    fun getHistoryById(id:Long) = local(Dispatchers.IO){
        dao.getHistoryById(id)
    }
    fun getHistoryByTitle(title:String) = local(Dispatchers.IO){
        dao.getHistoryByTitle(title)
    }
    fun enhanceImage(api:String,image:String) = fire(Dispatchers.IO){
        val result = ImageNetwork.enhanceImage(api,image)
        if ( result.image.isNullOrEmpty() ){
            Result.failure<RuntimeException>(
                RuntimeException("返回错误")
            )
        }else{
            Result.success(result)
        }
    }
    fun scanOcr(api:String,image:String) = fire(Dispatchers.IO){
        val result = ImageNetwork.scanOcr(api,image)
        if ( result.wordsResult.isNullOrEmpty() ){
            Result.failure<RuntimeException>(
                RuntimeException("返回错误")
            )
        }else{
            Result.success(result)
        }
    }
}

