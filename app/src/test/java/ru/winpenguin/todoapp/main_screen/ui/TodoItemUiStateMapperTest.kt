package ru.winpenguin.todoapp.main_screen.ui

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.TestLocales.RUSSIAN_LOCALE
import ru.winpenguin.todoapp.domain.models.Deadline
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.utils.DateFormatter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class TodoItemUiStateMapperTest {

    private val dateFormatter = DateFormatter(locale = RUSSIAN_LOCALE)
    private val sut = TodoItemUiStateMapper(dateFormatter)

    @Test
    fun `map not done item with low importance`() {
        val item = TodoItem(
            id = "1",
            isDone = false,
            text = "Text",
            importance = Importance.LOW,
            creationDate = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0, 0)
        )

        val uiState = sut.map(item)

        assertEquals(
            TodoItemUiState(
                id = "1",
                isChecked = false,
                checkBoxColorRes = R.color.checkbox_usual_colors,
                text = "Text",
                textColorAttr = R.color.light_label_primary,
                isStrikedThrough = false,
                priorityIconRes = R.drawable.low_priority,
                additionalText = null
            ),
            uiState
        )
    }

    @Test
    fun `map done item with high importance and not passed deadline`() {
        val today = LocalDate.of(2023, Month.JUNE, 5)
        val item = TodoItem(
            id = "1",
            isDone = true,
            text = "Text",
            importance = Importance.HIGH,
            creationDate = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0, 0),
            deadline = Deadline.Selected(LocalDate.of(2023, Month.JUNE, 7))
        )

        val uiState = sut.map(item, today)

        assertEquals(
            TodoItemUiState(
                id = "1",
                isChecked = true,
                checkBoxColorRes = R.color.checkbox_usual_colors,
                text = "Text",
                textColorAttr = R.color.light_label_tertiary,
                isStrikedThrough = true,
                priorityIconRes = R.drawable.high_priority,
                additionalText = "7 июня 2023"
            ),
            uiState
        )
    }

    @Test
    fun `map not done item with normal importance and passed deadline`() {
        val today = LocalDate.of(2023, Month.JUNE, 3)
        val item = TodoItem(
            id = "1",
            isDone = false,
            text = "Text",
            importance = Importance.NORMAL,
            creationDate = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0, 0),
            deadline = Deadline.Selected(LocalDate.of(2023, Month.JUNE, 2))
        )

        val uiState = sut.map(item, today)

        assertEquals(
            TodoItemUiState(
                id = "1",
                isChecked = false,
                checkBoxColorRes = R.color.checkbox_passed_deadline_colors,
                text = "Text",
                textColorAttr = R.color.light_label_primary,
                isStrikedThrough = false,
                priorityIconRes = null,
                additionalText = "2 июня 2023"
            ),
            uiState
        )
    }
}