package com.presentation.notes


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEAF3))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Detalles de la nota",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    actions = {
                        if (note != null) {
                            IconButton(onClick = { onEditClick(note) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            if (note == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
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
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(note.mood.iconRes),
                        contentDescription = note.mood.displayName,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = note.mood.displayName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val localDateTime = note.createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
                        Text(
                            text = "${localDateTime.dayOfMonth.toString().padStart(2, '0')}/" +
                                    "${localDateTime.monthNumber.toString().padStart(2, '0')}/${localDateTime.year} " +
                                    "a las ${localDateTime.hour}:${localDateTime.minute.toString().padStart(2, '0')}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Solo mostramos la sección si contiene texto
                if (note.feelingText.isNotBlank()) {
                    NoteDetailSection(label = "Te sentís:", value = note.feelingText)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (note.causeText.isNotBlank()) {
                    NoteDetailSection(label = "Debido a:", value = note.causeText)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (note.description.isNotBlank()) {
                    NoteDetailSection(label = "Descripción:", value = note.description)
                }
            }
        }
    }
}

@Composable
private fun NoteDetailSection(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}