package ru.winpenguin.todoapp.main_screen.ui

import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.domain.models.Deadline
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.utils.DateFormatter
import java.time.LocalDate

class TodoItemUiStateMapper(
    private val dateFormatter: DateFormatter,
) {

    fun map(items: List<TodoItem>): List<TodoItemUiState> {
        return items.map { item -> map(item) }
    }

    fun map(
        item: TodoItem,
        today: LocalDate = LocalDate.now(),
    ): TodoItemUiState {
        return TodoItemUiState(
            id = item.id,
            isChecked = item.isDone,
            checkBoxColorRes = when {
                item.deadline is Deadline.NotSelected -> {
                    R.color.checkbox_usual_colors
                }
                item.deadline is Deadline.Selected && item.deadline.date.isAfter(today) -> {
                    R.color.checkbox_usual_colors
                }
                else -> {
                    R.color.checkbox_passed_deadline_colors
                }
            },
            text = item.text,
            textColorAttr = if (item.isDone) R.attr.labelTertiary else R.attr.labelPrimary,
            isStrikedThrough = item.isDone,
            priorityIconRes = when (item.importance) {
                Importance.LOW -> R.drawable.low_priority
                Importance.NORMAL -> null
                Importance.HIGH -> R.drawable.high_priority
            },
            additionalText = when (item.deadline) {
                is Deadline.NotSelected -> null
                is Deadline.Selected -> dateFormatter.formatDate(item.deadline.date)
            }
        )
    }
}
