package com.example.notesapplication.ui.theme.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.notesapplication.repository.NoteRepository
import com.example.notesapplication.viewModel.AddEditNotesViewModel
import com.example.notesapplication.viewModel.NotesViewModelList

class ViewModelFactory(private val repository: NoteRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when{
            modelClass.isAssignableFrom(NotesViewModelList::class.java)->{
                NotesViewModelList(repository)as  T
            }
            modelClass.isAssignableFrom(AddEditNotesViewModel::class.java)->{
                val savedStateHandle = extras.createSavedStateHandle()
                AddEditNotesViewModel(repository,savedStateHandle) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}