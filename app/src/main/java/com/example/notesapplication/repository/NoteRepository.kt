package com.example.notesapplication.repository

import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.notesapplication.local.NoteEntity
import com.example.notesapplication.local.NotesDao
import com.example.notesapplication.preferences.PreferencesRepositoryNotes
import com.example.notesapplication.preferences.SortOrder
import kotlinx.coroutines.flow.Flow

class NoteRepository(
    private val dao: NotesDao,
    private val preferencesRepositoryNotes: PreferencesRepositoryNotes
) {
    val isDarkTheme: Flow<Boolean> = preferencesRepositoryNotes.isDarkTheme
    val sortOrder: Flow<SortOrder> = preferencesRepositoryNotes.sortOrder
    fun getNotesAndOrder(searchQuery: String, sortOrder: SortOrder): Flow<List<NoteEntity>>{
        return when(sortOrder){
            SortOrder.BY_DATE -> dao.getNoteSortedByDate(searchQuery)
            SortOrder.BY_PRIORITY -> dao.getNotePriority(searchQuery)
        }
    }
    //اضافة وتحديث الملاحظة
    suspend fun insertNote(notes: NoteEntity) = dao.insertNote(notes)
    suspend fun updateNote(notes: NoteEntity)= dao.editNote(notes)
    //حذف الملاحظة
    suspend fun deleteNotes(notes: NoteEntity) = dao.deleteNote(notes)
    //تعديل الملاحظة من ID الخاص بة
    suspend fun getById(id: Int): NoteEntity? = dao.getNoteById(id)
    //حفظ التغييرات
    suspend fun setDark(isDark: Boolean){
        preferencesRepositoryNotes.setDarkTheme(isDark)
    }
    suspend fun setSortOrder(order: SortOrder){
        preferencesRepositoryNotes.setSortOrder(order)
    }

}