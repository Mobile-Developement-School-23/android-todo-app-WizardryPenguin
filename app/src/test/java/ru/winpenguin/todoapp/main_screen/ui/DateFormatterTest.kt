package ru.winpenguin.todoapp.main_screen.ui

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.winpenguin.todoapp.TestLocales.RUSSIAN_LOCALE
import ru.winpenguin.todoapp.utils.DateFormatter
import java.time.LocalDate
import java.time.Month

class DateFormatterTest {

    private val dateFormatter = DateFormatter(locale = RUSSIAN_LOCALE)

    @Test
    fun `date with 1 digit day`() {
        val date = LocalDate.of(2023, Month.JUNE, 5)

        val formattedDate = dateFormatter.formatDate(date)

        assertEquals("5 июня 2023", formattedDate)
    }

    @Test
    fun `date with 2 digits day`() {
        val date = LocalDate.of(2023, Month.MAY, 23)

        val formattedDate = dateFormatter.formatDate(date)

        assertEquals("23 мая 2023", formattedDate)
    }
}