package com.mobilescanner.main.feature_project.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobilescanner.main.main.data.utils.FileUtils
import java.io.File

@Entity(tableName = "History")
data class ImageItem(
    val filePath:String,
    val title:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L
}
fun File.toImageItem() = ImageItem(absolutePath,FileUtils.getCurrentTime())
fun ImageItem.toFile() = File(filePath)
