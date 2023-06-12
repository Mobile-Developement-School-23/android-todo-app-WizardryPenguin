package ru.winpenguin.todoapp.main_screen.ui

import ru.winpenguin.todoapp.Importance
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.TodoItem
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
            checkBoxColorRes = if (item.deadline == null || item.deadline.isAfter(today)) {
                R.color.checkbox_usual_colors
            } else {
                R.color.checkbox_passed_deadline_colors
            },
            text = item.text,
            textColorRes = if (item.isDone) R.color.label_tertiary else R.color.label_primary,
            isStrikedThrough = item.isDone,
            priorityIconRes = when (item.importance) {
                Importance.LOW -> R.drawable.low_priority
                Importance.NORMAL -> null
                Importance.HIGH -> R.drawable.high_priority
            },
            additionalText = if (item.deadline == null) null else dateFormatter.formatDate(item.deadline)
        )
    }
}
