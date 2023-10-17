package com.mobilescanner.main.feature_home.data.item

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Note")
data class NoteItem(
    @PrimaryKey val id:Long = 0L,
    var title:String = "",
    var body:String = "",
    var time:String = ""
)