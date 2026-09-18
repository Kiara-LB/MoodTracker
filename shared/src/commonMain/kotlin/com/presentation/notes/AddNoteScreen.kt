package com.presentation.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.domain.model.Mood
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddNoteScreen(
    onNoteSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: AddNoteViewModel = koinViewModel()
) {
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var feelingText by remember { mutableStateOf("") }
    var causeText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val uiState = viewModel.uiState

    LaunchedEffect(uiState.success) {
        if (uiState.success) onNoteSaved()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "¿Cómo te sentís hoy?",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de mood — reemplazar por imágenes cuando tengas los assets
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

        if (uiState.error != null) {
            Text(
                text = uiState.error,
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
                    selectedMood?.let { mood ->
                        viewModel.saveNote(mood, feelingText, causeText, description)
                    }
                },
                enabled = !uiState.isLoading && selectedMood != null && description.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}