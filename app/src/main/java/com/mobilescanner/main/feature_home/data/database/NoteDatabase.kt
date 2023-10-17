package com.mobilescanner.main.feature_home.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobilescanner.main.feature_home.data.dao.NoteDao
import com.mobilescanner.main.feature_home.data.item.NoteItem

@Database(
    version = 1,
    entities = [NoteItem::class],
    exportSchema = false
)
abstract class NoteDataBase: RoomDatabase(){
    abstract fun NoteDao(): NoteDao
    val dao get() = NoteDao()
    companion object{
        private var instance: NoteDataBase ?= null
        @Synchronized
        fun getDataBase(context: Context): NoteDataBase {
            instance?.let { return it }
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = NoteDataBase::class.java,
                name = "NoteDataBase"
            ).build().apply { instance = this }
        }
    }
}