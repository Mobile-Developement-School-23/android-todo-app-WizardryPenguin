package ru.winpenguin.todoapp.main_screen.ui

import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.utils.DateFormatter
import java.time.Instant

class TodoItemUiStateMapper(
    private val dateFormatter: DateFormatter,
) {

    fun map(items: List<TodoItem>): List<TodoItemUiState> {
        return items.mapIndexed { index, item ->
            val position = getItemPosition(items.size, index)
            map(item, position)
        }
    }

    private fun getItemPosition(itemsCount: Int, index: Int): ItemPosition {
        return when {
            itemsCount == 1 -> ItemPosition.ONLY
            index == 0 -> ItemPosition.FIRST
            index == itemsCount - 1 -> ItemPosition.LAST
            else -> ItemPosition.MIDDLE
        }
    }

    fun map(
        item: TodoItem,
        position: ItemPosition,
        today: Instant = Instant.now(),
    ): TodoItemUiState {
        return TodoItemUiState(
            id = item.id,
            isChecked = item.isDone,
            checkBoxColorRes = when {
                item.deadline == null -> {
                    R.color.checkbox_usual_colors
                }
                item.deadline.isAfter(today) -> {
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
            additionalText = item.deadline?.let { dateFormatter.formatDate(item.deadline) },
            backgroundRes = when (position) {
                ItemPosition.ONLY -> R.drawable.round_corners_bg
                ItemPosition.FIRST -> R.drawable.round_top_corners_bg
                ItemPosition.LAST -> R.drawable.round_bottom_corners_bg
                ItemPosition.MIDDLE -> R.drawable.rectangle_bg
            }
        )
    }
}
