package com.example.notpad.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notpad.model.Note


@Dao
interface NoteDao {


    //Not işlemleri
    /*
    TOPLU BİÇİMDE EKLEME OLURSA BUNU EKLİCEZ
    @Insert
    suspend fun insertAll(vararg notes : Note) : List<Long>

    // vararg -> Sayısı belli olmayan durumlarda ne kadar geleceği belli değilse vararg kullanılır
    // List<Long> id döndürür
    */

    /*
    NoteAddViewModel KULLANILICAKLAR
     */
    @Insert
    suspend fun insertNote(note : Note)


    /*
    MainViewModel KULLANILICAKLAR
     */
    @Query("SELECT * FROM noteTable ORDER BY arrangementDate DESC")
    suspend fun getAllNotes() : List<Note>


    /*
    NoteDetailsViewModel KULLANILICAKLAR
     */

    @Query("SELECT * FROM noteTable WHERE uuid = :noteId ")
    suspend fun getNotes(noteId : Int) : Note

    @Query("DELETE FROM noteTable Where uuid = :noteId")
    suspend fun deleteNotes(noteId : Int)

    @Query("UPDATE noteTable SET arrangementDate = :newDate Where uuid =:noteId")
    suspend fun updateDateNotes(noteId :Int,newDate : String )

    @Query("UPDATE noteTable SET head = :newHead,content = :newContent Where uuid =:noteId")
    suspend fun updateNotes(noteId :Int,newHead : String ,newContent :String)


}