package com.example.notesapplication.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("""
        SELECT * FROM notes_table 
        WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'
        ORDER BY createdAt DESC
    """)
    fun getNoteSortedByDate(searchQuery: String): Flow<List<NoteEntity>>
    @Query("""
        SELECT * FROM notes_table 
        WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'
        ORDER BY CASE 
        WHEN priority = 'HIGH' THEN 1 
            WHEN priority = 'MEDIUM' THEN 2 
            WHEN priority = 'LOW' THEN 3 
        END ASC
    """)
    fun  getNotePriority(searchQuery: String): Flow<List<NoteEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(entity: NoteEntity)
    @Update
    suspend fun editNote(entity: NoteEntity)
    @Delete
    suspend fun deleteNote(entity: NoteEntity)
    @Query("SELECT * FROM notes_table WHERE id = :id ")
    suspend fun getNoteById(id: Int): NoteEntity?

}