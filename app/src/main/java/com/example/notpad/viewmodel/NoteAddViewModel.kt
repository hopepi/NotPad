package com.example.notpad.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notpad.model.Note
import com.example.notpad.service.NoteDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class NoteAddViewModel @Inject constructor(application: Application) : BaseViewModel(application){



    fun newNoteAddSQLite(note : Note) {
        viewModelScope.launch (Dispatchers.IO){
            val dao = NoteDatabase(getApplication()).noteDao()
            dao.insertNote(note)
        }
    }

    fun dateTime() : String{
        val now = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return now.format(format)
    }

}