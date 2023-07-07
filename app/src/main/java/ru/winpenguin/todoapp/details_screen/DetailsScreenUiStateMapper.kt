package ru.winpenguin.todoapp.details_screen

import ru.winpenguin.todoapp.domain.models.TodoItem
import javax.inject.Inject

/**
 * Преобразует данные из доменной модели в ui модель для детального экрана
 */
class DetailsScreenUiStateMapper @Inject constructor() {

    fun map(item: TodoItem?): DetailsScreenUiState {
        return if (item == null) {
            DetailsScreenUiState()
        } else {
            DetailsScreenUiState(
                text = item.text,
                importance = item.importance,
                isRemoveEnabled = true
            )
        }
    }
}