package ru.winpenguin.todoapp.main_screen

import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.utils.DateFormatter
import java.time.Instant
import javax.inject.Inject

/**
 * Преобразует данные из доменной модели для ui модели дела
 */
class TodoItemUiStateMapper @Inject constructor(
    private val dateFormatter: DateFormatter,
) {

    fun map(items: List<TodoItem>): List<TodoItemUiState> {
        return items.map { map(it) }
    }

    fun map(
        item: TodoItem,
        today: Instant = Instant.now(),
    ): TodoItemUiState {
        return TodoItemUiState(
            id = item.id,
            isChecked = item.isDone,
            text = item.text,
            isCheckboxHighlighted = when {
                item.deadline == null -> false
                item.deadline.isAfter(today) -> false
                else -> true
            },
            priorityIconRes = when (item.importance) {
                Importance.LOW -> R.drawable.low_priority
                Importance.NORMAL -> null
                Importance.HIGH -> R.drawable.high_priority
            },
            additionalText = item.deadline?.let { dateFormatter.formatDate(item.deadline) }
        )
    }
}
