package com.presentation.notes

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime

fun formatNoteDate(createdAt: kotlin.time.Instant): String {
    val timeZone = TimeZone.currentSystemDefault()
    val noteDateTime = createdAt.toLocalDateTime(timeZone)
    val noteDate = noteDateTime.date
    val today = kotlin.time.Clock.System.todayIn(timeZone)

    val hour = noteDateTime.hour.toString().padStart(2, '0')
    val minute = noteDateTime.minute.toString().padStart(2, '0')
    val time = "$hour:$minute"

    val datePart = when {
        noteDate == today -> "Hoy"
        noteDate == today.minus(1, kotlinx.datetime.DateTimeUnit.DAY) -> "Ayer"
        noteDate.year == today.year -> {
            val day = noteDate.day.toString().padStart(2, '0')
            val month = noteDate.month.number.toString().padStart(2, '0')
            "$day/$month"
        }
        else -> {
            val day = noteDate.day.toString().padStart(2, '0')
            val month = noteDate.month.number.toString().padStart(2, '0')
            "$day/$month/${noteDate.year}"
        }
    }

    return "$datePart - $time"
}