package com.mobilescanner.main.feature_home.data.repository

import com.mobilescanner.main.feature_home.data.database.NoteDataBase
import com.mobilescanner.main.feature_home.data.item.NoteItem
import com.mobilescanner.main.main.data.repository.BaseRepository
import com.mobilescanner.main.main.ui.MobileScannerApplication
import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread

object NoteRepository : BaseRepository() {
    private val dao = NoteDataBase.getDataBase(MobileScannerApplication.context).dao
    fun insertNote(item: NoteItem) = thread{
        dao.insertNote(item)
    }
    fun updateNote(newItem: NoteItem) =  thread {
        dao.updateNote(newItem)
    }
    fun deleteNote(item: NoteItem) = thread {
        dao.deleteNote(item)
    }
    fun deleteNoteByTitle(title:String) = thread {
        dao.deleteNoteByTitle(title)
    }
    fun deleteAllNote() = local(Dispatchers.IO) {
        dao.deleteAllNote()
    }
    fun loadAllNote() = local(Dispatchers.IO){
        dao.loadAllNote()
    }
    fun getNoteById(id:Long) = local(Dispatchers.IO){
        dao.getNoteById(id)
    }
    fun getNoteByTitle(title:String) = local(Dispatchers.IO){
        dao.getNoteByTitle(title)
    }
}