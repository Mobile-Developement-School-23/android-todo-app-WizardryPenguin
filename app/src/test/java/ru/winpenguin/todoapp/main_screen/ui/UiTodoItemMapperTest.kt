package ru.winpenguin.todoapp.main_screen.ui

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.winpenguin.todoapp.Importance
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.TestLocales.RUSSIAN_LOCALE
import ru.winpenguin.todoapp.TodoItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class UiTodoItemMapperTest {

    private val dateFormatter = DateFormatter(locale = RUSSIAN_LOCALE)
    private val sut = UiTodoItemMapper(dateFormatter)

    @Test
    fun `map not done item with low importance`() {
        val item = TodoItem(
            id = "1",
            isDone = false,
            text = "Text",
            importance = Importance.LOW,
            creationDate = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0, 0)
        )

        val uiItem = sut.map(item)

        assertEquals(
            UiTodoItem(
                id = "1",
                isChecked = false,
                checkBoxColorRes = R.color.checkbox_usual_colors,
                text = "Text",
                textColorRes = R.color.label_primary,
                isStrikedThrough = false,
                leadIconRes = R.drawable.low_priority,
                additionalText = null
            ),
            uiItem
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
            deadline = LocalDate.of(2023, Month.JUNE, 7)
        )

        val uiItem = sut.map(item, today)

        assertEquals(
            UiTodoItem(
                id = "1",
                isChecked = true,
                checkBoxColorRes = R.color.checkbox_usual_colors,
                text = "Text",
                textColorRes = R.color.label_tertiary,
                isStrikedThrough = true,
                leadIconRes = R.drawable.high_priority,
                additionalText = "7 июня 2023"
            ),
            uiItem
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
            deadline = LocalDate.of(2023, Month.JUNE, 2)
        )

        val uiItem = sut.map(item, today)

        assertEquals(
            UiTodoItem(
                id = "1",
                isChecked = false,
                checkBoxColorRes = R.color.checkbox_passed_deadline_colors,
                text = "Text",
                textColorRes = R.color.label_primary,
                isStrikedThrough = false,
                leadIconRes = null,
                additionalText = "2 июня 2023"
            ),
            uiItem
        )
    }
}