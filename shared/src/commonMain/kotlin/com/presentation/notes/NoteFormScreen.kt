package com.presentation.notes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val background = Color(0xFFF1ECF5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (noteToEdit == null) "¿Cómo te sentís hoy?" else "Editar nota",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Mood.entries.forEach { mood ->
                    MoodFilterChip(
                        mood = mood,
                        isSelected = selectedMood == mood,
                        onClick = { selectedMood = mood }
                    )
                }
            }


            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = feelingText,
                onValueChange = { if (it.length <= 10) feelingText = it },
                supportingText = { Text("${feelingText.length}/10") },
                placeholder = { Text("¿Cómo te sentís?") },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = causeText,
                onValueChange = { if (it.length <= 20) causeText = it },
                placeholder = { Text("¿Debido a qué?") },
                supportingText = { Text("${causeText.length}/20") },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { if (it.length <= 200) description = it },
                placeholder = { Text("Descripción") },
                supportingText = { Text("${description.length}/200") },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
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
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()


        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                border = BorderStroke(1.dp, Color(0xFFB083BB)),
                colors = ButtonDefaults.outlinedButtonColors(Color.Transparent)

            ) {
                Text("Cancelar", color = Color(0xFFB083BB))
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
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(Color(0xFFB083BB))
            ) {
                if (uiState.isSaving) {
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