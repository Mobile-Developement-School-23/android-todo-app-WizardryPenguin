package ru.winpenguin.todoapp.details_screen

import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.utils.DateFormatter
import javax.inject.Inject

/**
 * Преобразует данные из доменной модели в ui модель для детального экрана
 */
class DetailsScreenUiStateMapper @Inject constructor(
    private val dateFormatter: DateFormatter
) {

    fun map(item: TodoItem?): DetailsScreenUiState {
        return if (item == null) {
            DetailsScreenUiState()
        } else {
            DetailsScreenUiState(
                description = item.text,
                importance = item.importance,
                isRemoveEnabled = true,
                deadline = item.deadline?.let { dateFormatter.formatDate(it) },
                isDeadlineChecked = item.deadline != null,
                isCalendarVisible = false
            )
        }
    }
}