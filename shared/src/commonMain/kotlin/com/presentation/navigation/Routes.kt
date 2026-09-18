package com.presentation.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable object Login : Routes()
    @Serializable object Register : Routes()
    @Serializable object Home : Routes()
    @Serializable object Notes : Routes()
    @Serializable object Profile : Routes()
    @Serializable object AddNote : Routes()
}