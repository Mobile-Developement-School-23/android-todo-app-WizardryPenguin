package ru.winpenguin.todoapp.main_screen

import androidx.annotation.DrawableRes

data class TodoItemUiState(
    val id: String,
    val isChecked: Boolean,
    @DrawableRes val priorityIconRes: Int?,
    val text: String,
    val additionalText: String?,
    val isCheckboxHighlighted: Boolean
)