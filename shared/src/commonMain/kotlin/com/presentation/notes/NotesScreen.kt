package com.presentation.notes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.domain.model.Mood
import com.domain.model.Note
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NotesScreen(
    onEditNote: (Note) -> Unit,
    viewModel: NotesViewModel = koinViewModel(),
    onNoteClick: (Note) -> Unit,
    onCardClick: (Note) -> Unit,

    ) {
    val uiState = viewModel.uiState
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val filteredNotes = remember(uiState.notes, selectedMood, selectedDateMillis) {
        uiState.notes.filter { note ->
            val matchesMood = selectedMood == null || note.mood == selectedMood
            val matchesDate = selectedDateMillis == null || isSameDay(note.createdAt, selectedDateMillis!!)
            matchesMood && matchesDate
        }
    }
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF3D5F6), Color(0xFFFCE4EC))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp, 16.dp, 16.dp, 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                onClick = { showDatePicker = true },
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp, 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = selectedDateMillis?.let { formatMillisAsDate(it) } ?: "Filtrar por fecha"
                                    )
                                }
                            }

                            if (selectedDateMillis != null) {
                                IconButton(onClick = { selectedDateMillis = null }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Quitar filtro de fecha")
                                }
                            }
                        }
                    }

                    item {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                onClick = { selectedMood = null },
                                shape = RoundedCornerShape(50),
                                color = if (selectedMood == null) Color.DarkGray else Color.LightGray,
                                modifier = Modifier.height(40.dp)
                            ) {
                                Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                                    Text("Todos", color = Color.White, style = MaterialTheme.typography.labelLarge)
                                }
                            }

                            Mood.entries.forEach { mood ->
                                MoodFilterChip(
                                    mood = mood,
                                    isSelected = selectedMood == mood,
                                    onClick = { selectedMood = if (selectedMood == mood) null else mood }
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    if (filteredNotes.isEmpty()) {
                        item {
                            Text(
                                text = if (uiState.notes.isEmpty())
                                    "Todavía no cargaste ninguna nota"
                                else
                                    "No hay notas que coincidan con el filtro",
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(filteredNotes, key = { it.id }) { note ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                NoteCard(
                                    note = note,
                                    onEditClick = onEditNote,
                                    onDeleteConfirmed = { viewModel.deleteNote(it.id) },
                                    onCardClick = onCardClick,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
@Composable
private fun MoodFilterChip(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        mood.color.darken(0.25f)
    } else {
        mood.color
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        modifier = Modifier.height(40.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mood.displayName,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

private fun Color.darken(factor: Float): Color {
    return Color(
        red = red * (1 - factor),
        green = green * (1 - factor),
        blue = blue * (1 - factor),
        alpha = alpha
    )
}
private fun isSameDay(instant: Instant, millisToCompare: Long): Boolean {
    val timeZone = TimeZone.currentSystemDefault()
    val noteDate = instant.toLocalDateTime(timeZone).date
    val compareDate = Instant.fromEpochMilliseconds(millisToCompare).toLocalDateTime(TimeZone.UTC).date
    return noteDate == compareDate
}

private fun formatMillisAsDate(millis: Long): String {
    val date = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date
    return "${date.dayOfMonth.toString().padStart(2, '0')}/" +
            "${date.monthNumber.toString().padStart(2, '0')}/${date.year}"
}