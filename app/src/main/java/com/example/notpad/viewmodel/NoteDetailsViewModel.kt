package com.example.notpad.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.notpad.model.Note
import com.example.notpad.service.NoteDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(application: Application) : BaseViewModel(application ) {

    var note = mutableStateOf<Note?>(null)
        private set



    fun loadNoteDetailsSQLite(noteId : Int){
        viewModelScope.launch(Dispatchers.IO){
            val dao = NoteDatabase(getApplication()).noteDao()
            note.value = dao.getNotes(noteId)
        }
    }

    fun deleteNoteSQLite(noteId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            val dao = NoteDatabase(getApplication()).noteDao()
            dao.deleteNotes(noteId)
            /*
            Yönlendirme yapmalıyız
             */
        }
    }

    fun updateNoteSQLite(newHead : String, newContent : String){
        viewModelScope.launch (Dispatchers.IO){
            val dao = NoteDatabase(getApplication()).noteDao()
            dao.updateNotes(note.value!!.uuid ,newHead,newContent)
            //ÇALIŞMAYABİLİR KONTROL ETMEN GEREK
        }
    }



    /*
    Eğer değişiklik olursa çalışıcak
    buna sonra karar vericem
     */
    fun updateDateNoteSQLite(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val dao = NoteDatabase(getApplication()).noteDao()
            dao.updateDateNotes(id,dateTime())
        }
    }



    private fun dateTime() : String{
        val now = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return now.format(format)
    }
}