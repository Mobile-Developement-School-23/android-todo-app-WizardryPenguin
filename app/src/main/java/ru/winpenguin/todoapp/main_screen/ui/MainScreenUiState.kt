package ru.winpenguin.todoapp.main_screen.ui

import androidx.annotation.DrawableRes

data class MainScreenUiState(
    val todoItems: List<TodoItemUiState> = listOf(),
    val doneItemsCount: Int = 0,
    @DrawableRes val visibilityImageRes: Int = 0
)
