package com.mobilescanner.main.feature_project.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "Project")
@TypeConverters(MutableListConverter::class)
data class ProjectItem(
    @PrimaryKey(autoGenerate = true) val id:Long = 0,
    var title:String = "",
    var body:String = "",
    val imageList:MutableList<String> = ArrayList<String>()
)
class MutableListConverter{
    @TypeConverter
    fun listToString(list:MutableList<String>):String{
        return Gson().toJson(list)
    }
    @TypeConverter
    fun stringToList(str:String):MutableList<String>{
        val type = object :TypeToken<MutableList<String>>(){}.type
        return Gson().fromJson(str,type)
    }
}