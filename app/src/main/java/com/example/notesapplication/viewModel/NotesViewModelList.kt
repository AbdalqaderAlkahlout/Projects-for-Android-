package com.example.notesapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapplication.local.NoteEntity
import com.example.notesapplication.preferences.SortOrder
import com.example.notesapplication.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelList(val repository: NoteRepository): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val search : StateFlow<String> = _searchQuery.asStateFlow()
    //flow data in repository
    private val _notes = combine(
        _searchQuery,
        repository.sortOrder
    ){query,sort ->
        Pair(query,sort)
    }.flatMapLatest { (query,sort)->
        repository.getNotesAndOrder(searchQuery = query , sortOrder = sort)
    }
    val stateUi : StateFlow<NotesUiState> = combine(
        _notes,
        _searchQuery,
        repository.sortOrder,
        repository.isDarkTheme
    ){notes,query,sort,dark ->
        NotesUiState(
            notes = notes,
            searchQuery = query,
            sortOrder = sort,
            isDarkUi = dark
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState(isLoading = true)
    )
    fun onSearchChange(newQuery: String){
        _searchQuery.value = newQuery

    }
    // sort notes in 
    fun onSortOrderChange(sortOrder: SortOrder){
        viewModelScope.launch {
            repository.setSortOrder(sortOrder)
        }
    }
    //change ui dark or laght theme
    fun onToggleDarkTheme(newDark: Boolean){
        viewModelScope.launch {
            repository.setDark(newDark)
        }


    }
    // delete notes
    fun onToggleDelete(note: NoteEntity){
        viewModelScope.launch {
            repository.deleteNotes(note)
        }
    }
    //change state notes
    fun toggleNoteCompletion(notes: NoteEntity){
        viewModelScope.launch {
            repository.updateNote(notes.copy(isCompleted = !notes.isCompleted))
        }
    }

}