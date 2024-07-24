package com.example.notpad.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "noteTable")
data class Note(
    @ColumnInfo(name = "head")
    val noteHead : String?,
    @ColumnInfo(name = "content")
    val noteContent : String?,
    @ColumnInfo(name = "firstDate")
    val noteDate : String?,
    @ColumnInfo(name = "arrangementDate")
    val noteDateArrangement : String?
){
    /*Primary Key*/
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0
}

