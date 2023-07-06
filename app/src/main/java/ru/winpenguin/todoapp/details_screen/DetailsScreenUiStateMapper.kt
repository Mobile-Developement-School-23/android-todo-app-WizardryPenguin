package ru.winpenguin.todoapp.details_screen

import ru.winpenguin.todoapp.domain.models.TodoItem
import javax.inject.Inject

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