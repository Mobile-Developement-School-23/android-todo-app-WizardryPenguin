package ru.winpenguin.todoapp.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DateFormatter(
    localeProvider: () -> Locale = { Locale.getDefault() },
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "d MMMM yyyy",
        localeProvider()
    ),
    private val zoneIdProvider: () -> ZoneId = { ZoneId.systemDefault() }
) {

    fun formatDate(instant: Instant): String {
        val localDate = instant.atZone(zoneIdProvider()).toLocalDate()
        return localDate.format(dateFormatter)
    }
}