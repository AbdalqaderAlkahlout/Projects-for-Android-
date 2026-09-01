package com.example.notesapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.notesapplication.preferences.PreferencesRepositoryNotes
import com.example.notesapplication.ui.theme.NotesApplicationTheme
import com.example.notesapplication.ui.theme.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userPrefs= PreferencesRepositoryNotes(applicationContext)
            val isDarkTheme by userPrefs.isDarkTheme.collectAsState(initial = false)
            NotesApplicationTheme(darkTheme = isDarkTheme) {
                AppNavigation()

            }
        }
    }
}
