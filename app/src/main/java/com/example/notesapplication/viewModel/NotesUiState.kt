package com.example.notesapplication.viewModel

import com.example.notesapplication.local.NoteEntity
import com.example.notesapplication.preferences.SortOrder

data class NotesUiState(
    val notes : List<NoteEntity> = emptyList(),
    val searchQuery : String="",
    val sortOrder: SortOrder = SortOrder.BY_DATE,
    val isDarkUi : Boolean = false,
    val isLoading : Boolean = false
)
