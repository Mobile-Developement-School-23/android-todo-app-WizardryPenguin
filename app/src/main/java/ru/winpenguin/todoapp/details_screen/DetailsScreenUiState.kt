package ru.winpenguin.todoapp.details_screen

import ru.winpenguin.todoapp.domain.models.Importance

data class DetailsScreenUiState(
    val text: String = "",
    val importance: Importance = Importance.NORMAL,
    val isRemoveEnabled: Boolean = false
)
