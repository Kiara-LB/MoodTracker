package com.presentation.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable object Login : Routes()
    @Serializable data class NoteDetail(val noteId: String) : Routes()

    @Serializable object Register : Routes()
    @Serializable object Home : Routes()
    @Serializable object Notes : Routes()
    @Serializable object Profile : Routes()
    @Serializable data class NoteForm(val noteId: String? = null) : Routes()
}