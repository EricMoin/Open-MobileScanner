package com.mobilescanner.main.feature_project.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mobilescanner.main.feature_project.data.entity.ImageItem

@Dao
interface ImageDao {
    @Insert
    fun insertHistory(item: ImageItem)

    @Update
    fun updateHistory(newItem: ImageItem)

    @Query("SELECT * FROM History")
    fun loadAllHistory():List<ImageItem>
    @Query("SELECT * FROM History WHERE id = :id")
    fun getHistoryById(id:Long): ImageItem
    @Query("SELECT * FROM History WHERE title = :title")
    fun getHistoryByTitle(title:String): ImageItem

    @Delete
    fun deleteHistory(item: ImageItem)

    @Query("DELETE FROM History WHERE title = :title")
    fun deleteHistoryByTitle(title:String)

    @Query("DELETE FROM History")
    fun deleteAllHistory()
}