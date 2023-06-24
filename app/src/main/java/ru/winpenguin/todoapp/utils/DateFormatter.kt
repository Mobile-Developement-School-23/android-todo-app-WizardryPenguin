package ru.winpenguin.todoapp.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DateFormatter(
    locale: Locale = Locale.getDefault(),
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "d MMMM yyyy",
        locale
    ),
) {

    fun formatDate(date: LocalDate): String {
        return date.format(dateFormatter)
    }
}