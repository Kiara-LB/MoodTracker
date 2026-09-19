package com.presentation.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.domain.model.Mood
import com.domain.model.Note
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun NoteFormScreen(
    noteToEdit: Note?, // null = modo "crear", no-null = modo "editar"
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: NotesViewModel
) {
    var selectedMood by remember { mutableStateOf(noteToEdit?.mood) }
    var feelingText by remember { mutableStateOf(noteToEdit?.feelingText ?: "") }
    var causeText by remember { mutableStateOf(noteToEdit?.causeText ?: "") }
    var description by remember { mutableStateOf(noteToEdit?.description ?: "") }

    val uiState = viewModel.uiState
    var wasSaving by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaving) {
        if (wasSaving && !uiState.isSaving && uiState.error == null) {
            onSaved()
        }
        wasSaving = uiState.isSaving
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = if (noteToEdit == null) "¿Cómo te sentís hoy?" else "Editar nota",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Mood.entries) { mood ->
                FilterChip(
                    selected = selectedMood == mood,
                    onClick = { selectedMood = mood },
                    label = { Text(mood.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = feelingText,
            onValueChange = { feelingText = it },
            label = { Text("¿Cómo te sentís?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = causeText,
            onValueChange = { causeText = it },
            label = { Text("¿Debido a qué?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { if (it.length <= 200) description = it },
            label = { Text("Descripción") },
            supportingText = { Text("${description.length}/200") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        val errorMessage = uiState.error
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = {
                    val mood = selectedMood ?: return@Button
                    if (noteToEdit == null) {
                        viewModel.saveNote(mood, feelingText, causeText, description)
                    } else {
                        viewModel.updateNote(noteToEdit.id, mood, feelingText, causeText, description)
                    }
                },
                enabled = !uiState.isSaving && selectedMood != null && description.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (noteToEdit == null) "Guardar" else "Guardar cambios")
                }
            }
        }
    }
}