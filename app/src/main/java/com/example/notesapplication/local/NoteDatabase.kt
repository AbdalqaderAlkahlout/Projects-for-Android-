package com.example.notesapplication.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [NoteEntity::class], version = 1
    , exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NotesDao
    companion object{
        @Volatile
        private var INSTANCE: NoteDatabase? = null
        fun getDatabase(context: Context): NoteDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notes_database"

                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}