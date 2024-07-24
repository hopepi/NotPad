package com.example.notpad.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notpad.model.Note


@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao() :NoteDao

    /* Singleton yapma sebebim bu nesne farklı threadinglerde
    bu işlem yapılmaya çalışılırsa tek bir nesneye erişim olmasını sağlamaya çalışıyorum */

    companion object {
        // Volatile olarak tanımlanırsa bir değer farklı threadlerdede görünür hale gelir bu database

        @Volatile
        private var instance: NoteDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock = lock) {
            instance ?: databaseCreate(context).also {
                instance=it
            }
        }

        private fun databaseCreate(context: Context) = Room.databaseBuilder(
            context.applicationContext,NoteDatabase::class.java,"noteDatabase"
        ).build()
    }
}