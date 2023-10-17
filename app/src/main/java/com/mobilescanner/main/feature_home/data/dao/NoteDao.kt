package com.mobilescanner.main.feature_home.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mobilescanner.main.feature_home.data.item.NoteItem

@Dao
interface NoteDao {
    @Insert
    fun insertNote(item: NoteItem)

    @Update
    fun updateNote(newItem: NoteItem)

    @Query("SELECT * FROM Note")
    fun loadAllNote():List<NoteItem>
    @Query("SELECT * FROM Note WHERE id = :id")
    fun getNoteById(id:Long): NoteItem?
    @Query("SELECT * FROM Note WHERE title = :title")
    fun getNoteByTitle(title:String): NoteItem

    @Delete
    fun deleteNote(item: NoteItem)

    @Query("DELETE FROM Note WHERE title = :title")
    fun deleteNoteByTitle(title:String)

    @Query("DELETE FROM Note")
    fun deleteAllNote()
}