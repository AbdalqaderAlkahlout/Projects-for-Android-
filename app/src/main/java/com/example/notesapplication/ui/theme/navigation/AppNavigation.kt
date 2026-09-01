package com.example.notesapplication.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notesapplication.local.NoteDatabase
import com.example.notesapplication.preferences.PreferencesRepositoryNotes
import com.example.notesapplication.repository.NoteRepository
import com.example.notesapplication.ui.theme.screens.add_edit_notes.AddNotesEdit
import com.example.notesapplication.ui.theme.screens.add_edit_notes.NotesListScreen
import com.example.notesapplication.ui.theme.screens.add_edit_notes.SplashScreen
import com.example.notesapplication.viewModel.AddEditNotesViewModel
import com.example.notesapplication.viewModel.NotesViewModelList

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = NoteDatabase.getDatabase(context)
    val preferencesRepository = PreferencesRepositoryNotes(context)
    val repository = NoteRepository(database.noteDao(),preferencesRepository)
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ){
        composable(Screen.SplashScreen.route){
            SplashScreen {
                navController.navigate(Screen.NotesListScreen.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true } // Clear splash from stack
                }
            }
        }
        composable(Screen.NotesListScreen.route){
            val viewModel: NotesViewModelList = viewModel(
                factory = com.example.notesapplication.ui.theme.util.ViewModelFactory(repository = repository)
            )
            NotesListScreen(
                viewModel = viewModel,
                onAddNewNoteClick = {
                    navController.navigate(Screen.AddEditNotesScreen.passNotesById(-1))
                },
                onEditsNotesClick = { noteId ->
                    navController.navigate(Screen.AddEditNotesScreen.passNotesById(noteId))

                }

            )
        }
        composable(
            route = Screen.AddEditNotesScreen.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {
            val viewModel: AddEditNotesViewModel = viewModel(
                factory = com.example.notesapplication.ui.theme.util.ViewModelFactory(repository = repository)
            )
            AddNotesEdit(
                viewModel = viewModel,
                popBackStack = { navController.popBackStack() }

            )
        }

    }
}