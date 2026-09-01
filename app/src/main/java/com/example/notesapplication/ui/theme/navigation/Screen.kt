package com.example.notesapplication.ui.theme.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object NotesListScreen : Screen("notes_list_screen")
    object AddEditNotesScreen : Screen("add_edit_notes_screen?noteId={noteId}"){
        fun passNotesById(noteId: Int): String{
            return "add_edit_notes_screen?noteId=$noteId"
        }
    }

}