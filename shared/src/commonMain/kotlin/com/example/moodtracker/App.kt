package com.example.moodtracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.domain.repository.AuthRepository
import com.presentation.auth.LoginScreen
import com.presentation.auth.RegisterScreen
import com.presentation.home.HomeScreen
import com.presentation.navigation.Routes
import com.presentation.notes.NoteFormScreen
import com.presentation.notes.NotesScreen
import com.presentation.notes.NotesViewModel
import com.presentation.profile.ProfileScreen
import io.github.jan.supabase.auth.status.SessionStatus
import org.jetbrains.compose.resources.painterResource

import moodtracker.shared.generated.resources.Res
import moodtracker.shared.generated.resources.compose_multiplatform
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val mainRoutes = setOf(
        Routes.Home::class.qualifiedName,
        Routes.Notes::class.qualifiedName,
        Routes.Profile::class.qualifiedName
    )

    val showBottomBar = currentRoute in mainRoutes
    val notesViewModel: NotesViewModel = koinViewModel()
    val authRepository: AuthRepository = koinInject()
    var isDarkTheme by remember { mutableStateOf(false) }

    val sessionStatus by authRepository.sessionStatus.collectAsState(initial = SessionStatus.Initializing)

    when (val status = sessionStatus) {
        is SessionStatus.Initializing -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {
            val startDestination = if (status is SessionStatus.Authenticated) Routes.Home else Routes.Login


    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentRoute == Routes.Home::class.qualifiedName,
                            onClick = { navController.navigateToTab(Routes.Home) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == Routes.Notes::class.qualifiedName,
                            onClick = { navController.navigateToTab(Routes.Notes) },
                            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Notas") },
                            label = { Text("Notas") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == Routes.Profile::class.qualifiedName,
                            onClick = { navController.navigateToTab(Routes.Profile) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                            label = { Text("Perfil") }
                        )
                    }
                }
            }

        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<Routes.Login> {
                    LoginScreen(
                        onLoginSuccess = {
                            notesViewModel.loadNotes()
                            navController.navigate(Routes.Home) {
                                popUpTo(Routes.Login) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = { navController.navigate(Routes.Register) }
                    )
                }

                composable<Routes.Register> {
                    RegisterScreen(
                        onRegisterSuccess = { navController.popBackStack() }
                    )
                }

                composable<Routes.Home> {
                    HomeScreen(
                        notesViewModel = notesViewModel,
                        onAddNoteClick = { navController.navigate(Routes.NoteForm()) },
                        onViewNotesClick = { navController.navigate(Routes.Notes) },      onEditNoteClick = { note ->
                            navController.navigate(Routes.NoteForm(noteId = note.id))
                        }

                    )
                }

                composable<Routes.Notes> {
                    NotesScreen(
                        viewModel = notesViewModel,
                        onEditNote = { note ->
                            navController.navigate(Routes.NoteForm(noteId = note.id))
                        }
                    )
                }
                composable<Routes.Profile> {
                    ProfileScreen(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { isDarkTheme = it },
                        onLoggedOut = {
                            navController.navigate(Routes.Login) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable<Routes.NoteForm> { backStackEntry ->
                    val route: Routes.NoteForm = backStackEntry.toRoute()
                    val noteToEdit = route.noteId?.let { notesViewModel.findNoteById(it) }

                    NoteFormScreen(
                        noteToEdit = noteToEdit,
                        viewModel = notesViewModel,
                        onSaved = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }
            }
        }
    }
        }
    }
}

private fun NavHostController.navigateToTab(route: Routes) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}