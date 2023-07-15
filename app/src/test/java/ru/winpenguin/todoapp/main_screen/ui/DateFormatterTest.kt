package ru.winpenguin.todoapp.main_screen.ui

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.winpenguin.todoapp.TestLocales.RUSSIAN_LOCALE
import ru.winpenguin.todoapp.utils.DateFormatter
import ru.winpenguin.todoapp.utils.LocaleProvider
import ru.winpenguin.todoapp.utils.ZoneIdProvider
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class DateFormatterTest {

    private val localeProvider = object : LocaleProvider {
        override fun locale(): () -> Locale = { RUSSIAN_LOCALE }
    }
    private val zoneIdProvider = object : ZoneIdProvider {
        override fun zoneId(): () -> ZoneId = { ZoneId.of("Europe/Moscow") }
    }

    private val dateFormatter = DateFormatter(
        localeProvider,
        zoneIdProvider
    )

    @Test
    fun `date with 1 digit day`() {
        val date = Instant.parse("2023-06-05T12:00:00Z")

        val formattedDate = dateFormatter.formatDate(Instant.from(date))

        assertEquals("5 июня 2023", formattedDate)
    }

    @Test
    fun `date with 2 digits day`() {
        val date = Instant.parse("2023-05-23T12:00:00Z")

        val formattedDate = dateFormatter.formatDate(Instant.from(date))

        assertEquals("23 мая 2023", formattedDate)
    }
}