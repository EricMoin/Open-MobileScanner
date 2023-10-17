package com.mobilescanner.main.feature_project.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobilescanner.main.feature_project.data.dao.ProjectDao
import com.mobilescanner.main.feature_project.data.entity.ProjectItem

@Database(
    version = 1,
    entities = [ProjectItem::class],
    exportSchema = false
)
abstract class ProjectDataBase: RoomDatabase(){
    abstract fun projectDao(): ProjectDao
    val dao get() = projectDao()
    companion object{
        private var instance: ProjectDataBase ?= null
        @Synchronized
        fun getDataBase(context: Context): ProjectDataBase {
            instance?.let { return it }
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = ProjectDataBase::class.java,
                name = "ProjectDataBase"
            ).build().apply { instance = this }
        }
    }
}