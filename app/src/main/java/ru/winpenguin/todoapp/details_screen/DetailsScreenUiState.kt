package ru.winpenguin.todoapp.details_screen

import ru.winpenguin.todoapp.domain.models.Importance

data class DetailsScreenUiState(
    val description: String = "",
    val importance: Importance = Importance.NORMAL,
    val deadline: String? = null,
    val isDeadlineChecked: Boolean = false,
    val isRemoveEnabled: Boolean = false,
    val isCalendarVisible: Boolean = false,
)