package ru.winpenguin.todoapp.utils

import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateFormatter @Inject constructor(
    private val localeProvider: LocaleProvider,
    private val zoneIdProvider: ZoneIdProvider
) {

    fun formatDate(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern(
            PATTERN,
            localeProvider.locale().invoke()
        )
        val localDate = instant.atZone(zoneIdProvider.zoneId().invoke()).toLocalDate()
        return localDate.format(formatter)
    }

    companion object {
        private const val PATTERN = "d MMMM yyyy"
    }
}