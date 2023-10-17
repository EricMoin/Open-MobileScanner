package com.mobilescanner.main.feature_home.ui.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.mobilescanner.main.feature_home.data.item.NoteItem
import com.mobilescanner.main.feature_home.data.repository.NoteRepository

class NoteEditViewModel : ViewModel() {
    val noteList = mutableListOf<NoteItem>()
    private val _noteLiveData = MutableLiveData<Unit>()
    val noteLiveData get() = _noteLiveData.switchMap {
        NoteRepository.loadAllNote()
    }
    fun loadAllNote(){
        _noteLiveData.value = _noteLiveData.value
    }
    var noteItem = NoteItem( id = -1L , title = "", body = "", time = "" )
    fun saveNote() = NoteRepository.insertNote(noteItem)
    fun updateNote() = NoteRepository.updateNote(noteItem)
    private val _queryLiveData = MutableLiveData<Long>()
    val queryLiveData get() = _queryLiveData.switchMap { id ->
        NoteRepository.getNoteById(id)
    }
    fun deleteNote(item:NoteItem) = NoteRepository.deleteNote(item)
    fun getNoteById(id:Long){
        _queryLiveData.value = id
    }
}