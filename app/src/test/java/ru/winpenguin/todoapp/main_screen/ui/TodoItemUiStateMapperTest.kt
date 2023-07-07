package ru.winpenguin.todoapp.main_screen.ui

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.TestLocales.RUSSIAN_LOCALE
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.main_screen.ui.ItemPosition.FIRST
import ru.winpenguin.todoapp.main_screen.ui.ItemPosition.LAST
import ru.winpenguin.todoapp.main_screen.ui.ItemPosition.MIDDLE
import ru.winpenguin.todoapp.main_screen.ui.ItemPosition.ONLY
import ru.winpenguin.todoapp.utils.DateFormatter
import java.time.Instant
import java.time.ZoneId

class TodoItemUiStateMapperTest {

    private val dateFormatter = DateFormatter(
        localeProvider = { RUSSIAN_LOCALE },
        zoneIdProvider = { ZoneId.of("Europe/Moscow") }
    )
    private val sut = TodoItemUiStateMapper(dateFormatter)

    @Test
    fun `map not done item with low importance`() {
        val item = TodoItem(
            id = "1",
            isDone = false,
            text = "Text",
            importance = Importance.LOW,
            creationDate = Instant.parse("2023-06-01T12:00:00Z"),
            changeDate = Instant.parse("2023-06-01T12:00:00Z")
        )

        val uiState = sut.map(item, MIDDLE)

        assertEquals(
            TodoItemUiState(
                id = "1",
                isChecked = false,
                checkBoxColorRes = R.color.checkbox_usual_colors,
                text = "Text",
                textColorAttr = R.attr.labelPrimary,
                isStrikedThrough = false,
                priorityIconRes = R.drawable.low_priority,
                additionalText = null,
                backgroundRes = R.drawable.rectangle_bg
            ),
            uiState
        )
    }

    @Test
    fun `map done item with high importance and not passed deadline`() {
        val today = Instant.parse("2023-06-05T12:00:00Z")
        val item = TodoItem(
            id = "1",
            isDone = true,
            text = "Text",
            importance = Importance.HIGH,
            creationDate = Instant.parse("2023-06-01T12:00:00Z"),
            changeDate = Instant.parse("2023-06-01T12:00:00Z"),
            deadline = Instant.parse("2023-06-07T12:00:00Z")
        )

        val uiState = sut.map(item, LAST, today)

        assertEquals(
            TodoItemUiState(
                id = "1",
                isChecked = true,
                checkBoxColorRes = R.color.checkbox_usual_colors,
                text = "Text",
                textColorAttr = R.attr.labelTertiary,
                isStrikedThrough = true,
                priorityIconRes = R.drawable.high_priority,
                additionalText = "7 июня 2023",
                backgroundRes = R.drawable.round_bottom_corners_bg
            ),
            uiState
        )
    }

    @Test
    fun `map not done item with normal importance and passed deadline`() {
        val today = Instant.parse("2023-06-03T12:00:00Z")
        val item = TodoItem(
            id = "1",
            isDone = false,
            text = "Text",
            importance = Importance.NORMAL,
            creationDate = Instant.parse("2023-06-01T12:00:00Z"),
            changeDate = Instant.parse("2023-06-01T12:00:00Z"),
            deadline = Instant.parse("2023-06-02T12:00:00Z")
        )

        val uiState = sut.map(item, FIRST, Instant.from(today))

        assertEquals(
            TodoItemUiState(
                id = "1",
                isChecked = false,
                checkBoxColorRes = R.color.checkbox_passed_deadline_colors,
                text = "Text",
                textColorAttr = R.attr.labelPrimary,
                isStrikedThrough = false,
                priorityIconRes = null,
                additionalText = "2 июня 2023",
                backgroundRes = R.drawable.round_top_corners_bg
            ),
            uiState
        )
    }

    @Test
    fun `map the only one not done item with normal importance with not passed deadline`() {
        val today = Instant.parse("2023-06-15T12:00:00Z")
        val item = TodoItem(
            id = "1",
            isDone = false,
            text = "Text",
            importance = Importance.NORMAL,
            creationDate = Instant.parse("2023-06-10T12:00:00Z"),
            deadline = Instant.parse("2023-06-17T12:00:00Z"),
            changeDate = Instant.from(Instant.parse("2023-06-12T12:00:00Z"))
        )

        val uiState = sut.map(item, ONLY, Instant.from(today))

        assertEquals(
            TodoItemUiState(
                id = "1",
                isChecked = false,
                checkBoxColorRes = R.color.checkbox_usual_colors,
                text = "Text",
                textColorAttr = R.attr.labelPrimary,
                isStrikedThrough = false,
                priorityIconRes = null,
                additionalText = "17 июня 2023",
                backgroundRes = R.drawable.round_corners_bg
            ),
            uiState
        )
    }
}