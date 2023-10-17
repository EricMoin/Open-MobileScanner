package com.mobilescanner.main.feature_project.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobilescanner.main.feature_project.data.entity.ImageItem
import com.mobilescanner.main.feature_project.data.dao.ImageDao

@Database(
    version = 1,
    entities = [ImageItem::class],
    exportSchema = false
)
abstract class ImageItemDatabase:RoomDatabase(){
    abstract fun imageDao(): ImageDao
    val dao get() = imageDao()
    companion object{
        private var instance: ImageItemDatabase?= null
        @Synchronized
        fun getDataBase(context: Context): ImageItemDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = ImageItemDatabase::class.java,
                name = "History_DataBase"
            ).build().apply { instance = this }
        }
    }
}