package com.example.notesapplication.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapplication.ui.theme.util.Category
import com.example.notesapplication.ui.theme.util.Priority

@Entity(tableName = "notes_table")
data class NoteEntity(
@PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority = Priority.MEDIUM,
    val category: Category = Category.PERSONAL,
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)
