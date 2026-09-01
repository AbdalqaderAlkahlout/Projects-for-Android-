package com.example.notesapplication.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapplication.local.NoteEntity
import com.example.notesapplication.repository.NoteRepository
import com.example.notesapplication.ui.theme.util.Category
import com.example.notesapplication.ui.theme.util.Priority
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddEditNotesViewModel(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var priority by mutableStateOf(Priority.MEDIUM)
        private set
    var category by mutableStateOf(Category.PERSONAL)
        private set
    private  var currentNoteById: Int? = null
    private val  _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1){
                currentNoteById = noteId
                viewModelScope.launch {
                    repository.getById(noteId)?.let { note ->
                        title = note.title
                        description = note.description
                        priority = note.priority
                        category = note.category

                    }
                }
            }
        }
    }
    fun onTitleChange(newTitle: String){
        title = newTitle
    }
    fun onDescriptionChange(newDescription: String){
        description = newDescription
    }
    fun onPriorityChange(newPriority: Priority){
        priority = newPriority
    }
    fun onCategoryChange(newCategory: Category){
        category = newCategory

    }
    fun saveNote(){
        if (title.isBlank()){
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackBar("يرجى إدخال عنوان الملاحظة أولاً!"))
            }
            return
            }
        viewModelScope.launch {
            val note = NoteEntity(
                id = currentNoteById ?: 0,
                title = title.trim(),
                description = description.trim(),
                priority = priority,
                category = category,
                createdAt = System.currentTimeMillis()
            )
            if (currentNoteById == null){
                repository.insertNote(note)
            }else{
                repository.updateNote(note)
            }
            _uiEvent.emit(UiEvent.SaveSuccess)
        }
        }
    sealed interface UiEvent {
        object SaveSuccess: UiEvent
        data class  ShowSnackBar(val message: String): UiEvent

    }
    }

