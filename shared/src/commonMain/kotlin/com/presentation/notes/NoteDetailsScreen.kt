package com.presentation.notes


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.domain.model.Note
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note?,
    onBack: () -> Unit,
    onEditClick: (Note) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la nota") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (note != null) {
                        TextButton(onClick = { onEditClick(note) }) {
                            Text("Editar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (note == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontró la nota")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(note.mood.iconRes),
                    contentDescription = note.mood.displayName,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = note.mood.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            NoteDetailSection(label = "Te sentís", value = note.feelingText)
            Spacer(modifier = Modifier.height(16.dp))
            NoteDetailSection(label = "Debido a", value = note.causeText)
            Spacer(modifier = Modifier.height(16.dp))
            NoteDetailSection(label = "Descripción", value = note.description)

            Spacer(modifier = Modifier.height(24.dp))

            val localDateTime = note.createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
            Text(
                text = "Registrada el ${localDateTime.dayOfMonth.toString().padStart(2, '0')}/" +
                        "${localDateTime.monthNumber.toString().padStart(2, '0')}/${localDateTime.year} " +
                        "a las ${localDateTime.hour}:${localDateTime.minute.toString().padStart(2, '0')}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NoteDetailSection(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}