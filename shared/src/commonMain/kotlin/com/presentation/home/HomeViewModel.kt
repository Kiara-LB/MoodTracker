package com.presentation.home


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.domain.model.DayMood
import com.domain.model.MoodPercentage
import com.domain.model.Note
import com.domain.repository.AuthRepository
import com.domain.repository.ProfileRepository
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DateTimeUnit


class HomeViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    suspend fun updateStats(notes: List<Note>) {
        val userName = profileRepository.getProfile()
            .getOrNull()
            ?.name
            ?: "Usuario"

        uiState = uiState.copy(
            userName = userName,
            weeklyMoods = computeWeeklyMoods(notes),
            monthlyPercentages = computeMonthlyPercentages(notes),
            recentNotes = notes.sortedByDescending { it.createdAt }.take(3)
        )
    }
    private fun computeWeeklyMoods(notes: List<Note>): List<DayMood> {
        val timeZone = TimeZone.currentSystemDefault()
        val today = kotlin.time.Clock.System.todayIn(timeZone)
        val days = (6 downTo 0).map { offset -> today.minus(offset, DateTimeUnit.DAY) }

        return days.map { date ->
            val notesOfDay = notes.filter { it.createdAt.toLocalDateTime(timeZone).date == date }
            val lastMoodOfDay = notesOfDay.maxByOrNull { it.createdAt }?.mood
            DayMood(dayLabel = date.dayOfWeek.toSpanishAbbreviation(), mood = lastMoodOfDay)
        }
    }

    private fun computeMonthlyPercentages(notes: List<Note>): List<MoodPercentage> {
        val timeZone = TimeZone.currentSystemDefault()
        val today = kotlin.time.Clock.System.todayIn(timeZone)
        val notesThisMonth = notes.filter {
            val date = it.createdAt.toLocalDateTime(timeZone).date
            date.year == today.year && date.month.number == today.month.number
        }
        if (notesThisMonth.isEmpty()) return emptyList()

        val total = notesThisMonth.size
        return com.domain.model.Mood.entries.mapNotNull { mood ->
            val count = notesThisMonth.count { it.mood == mood }
            if (count == 0) null else MoodPercentage(mood, (count.toFloat() / total) * 100f)
        }
    }
}

private fun DayOfWeek.toSpanishAbbreviation(): String = when (this) {
    DayOfWeek.MONDAY -> "lun"
    DayOfWeek.TUESDAY -> "mar"
    DayOfWeek.WEDNESDAY -> "mié"
    DayOfWeek.THURSDAY -> "jue"
    DayOfWeek.FRIDAY -> "vie"
    DayOfWeek.SATURDAY -> "sáb"
    DayOfWeek.SUNDAY -> "dom"
    else -> ""
}

data class HomeUiState(
    val userName: String = "",
    val weeklyMoods: List<DayMood> = emptyList(),
    val monthlyPercentages: List<MoodPercentage> = emptyList(),
    val recentNotes: List<Note> = emptyList()
)