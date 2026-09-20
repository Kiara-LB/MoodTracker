package com.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.domain.model.DayMood
import com.domain.model.MotivationalPhrases
import com.domain.model.MoodPercentage
import com.domain.model.Note
import com.presentation.home.homeBackground.StarryBackgroundLayer
import com.presentation.notes.NoteCard
import com.presentation.notes.NotesViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import moodtracker.shared.generated.resources.Res
import moodtracker.shared.generated.resources.cloud
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
@Composable
fun HomeScreen(
    onAddNoteClick: () -> Unit,
    onViewNotesClick: () -> Unit,
    onEditNoteClick: (Note) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    notesViewModel: NotesViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    val notesUiState = notesViewModel.uiState
    val greeting = remember { greetingByTime() }
    val phrase = remember { MotivationalPhrases.list.random() }

    val recentNotes = remember(notesUiState.notes) {
        notesUiState.notes.sortedByDescending { it.createdAt }.take(3)
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEFD6EF),
            Color(0xFFFCE4EC)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        StarryBackgroundLayer(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "$greeting ${uiState.userName}!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFDBC4EF)
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(Res.drawable.cloud),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1.9f)
                            .align(Alignment.CenterEnd)
                            .offset(x = 90.dp)
                    )

                    Text(
                        text = phrase,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 90.dp)
                            .align(Alignment.CenterStart),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Mood scale de la semana",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            uiState.weeklyMoods.forEach { dayMood ->
                                WeeklyMoodItem(dayMood)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Moods del mes",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.monthlyPercentages.isEmpty() && !uiState.isLoading) {
                        Text(
                            text = "Todavía no hay notas este mes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        MonthlyMoodBarChart(uiState.monthlyPercentages)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tus notas",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onViewNotesClick) {
                    Text("Ver todas")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (recentNotes.isEmpty() && !notesUiState.isLoading) {
                Text(
                    text = "Todavía no creaste ninguna nota",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    recentNotes.forEach { note ->
                        NoteCard(
                            note = note,
                            onEditClick = onEditNoteClick,
                            onDeleteConfirmed = { notesViewModel.deleteNote(it.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(96.dp))
        }

        FloatingActionButton(
            onClick = onAddNoteClick,
            containerColor = Color(0xFFEED5EC),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)

        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar nota")
        }
    }
}
@Composable
private fun WeeklyMoodItem(dayMood: DayMood) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (dayMood.mood != null) {
                Image(
                    painter = painterResource(dayMood.mood.iconRes),
                    contentDescription = dayMood.mood.displayName,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = dayMood.dayLabel, style = MaterialTheme.typography.labelSmall)
    }
}
@Composable
private fun MonthlyMoodBarChart(percentages: List<MoodPercentage>) {
    val maxBarHeight = 100.dp
    val maxPercentage = percentages.maxOfOrNull { it.percentage } ?: 100f

    val extraContentHeight = 50.dp

    Row(
        modifier = Modifier.fillMaxWidth().height(maxBarHeight + extraContentHeight),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        percentages.forEach { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${item.percentage.toInt()}%",
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(maxBarHeight * (item.percentage / maxPercentage))
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .background(item.mood.color)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Image(
                    painter = painterResource(item.mood.iconRes),
                    contentDescription = item.mood.displayName,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
private fun greetingByTime(): String {
    val hour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
    return when {
        hour < 12 -> "Buenos días"
        hour < 20 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}