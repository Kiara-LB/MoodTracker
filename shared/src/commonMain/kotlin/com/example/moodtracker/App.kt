package com.example.moodtracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.presentation.auth.LoginScreen
import com.presentation.auth.RegisterScreen
import com.presentation.home.HomeScreen
import com.presentation.notes.AddNoteScreen
import com.presentation.notes.NotesScreen
import org.jetbrains.compose.resources.painterResource

import moodtracker.shared.generated.resources.Res
import moodtracker.shared.generated.resources.compose_multiplatform

//PROVISORIO!!! CAMBIAR
private enum class Screen { Register, Login, Home, AddNote, Notes }

@Composable
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(Screen.Login) }

        when (currentScreen) {
            Screen.Register -> RegisterScreen(
                onRegisterSuccess = { currentScreen = Screen.Login }
            )
            Screen.Login -> LoginScreen(
                onLoginSuccess = { currentScreen = Screen.Home },
                onNavigateToRegister = { currentScreen = Screen.Register }
            )
            Screen.Home -> HomeScreen(
                onAddNoteClick = { currentScreen = Screen.AddNote },
                onViewNotesClick = { currentScreen = Screen.Notes }
            )
            Screen.AddNote -> AddNoteScreen(
                onNoteSaved = { currentScreen = Screen.Home },
                onCancel = { currentScreen = Screen.Home }
            )
            Screen.Notes -> NotesScreen()
        }
    }
}