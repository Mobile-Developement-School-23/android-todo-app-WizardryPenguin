package ru.winpenguin.todoapp.details_screen

import ru.winpenguin.todoapp.domain.models.TodoItem

class DetailsScreenUiStateMapper {

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