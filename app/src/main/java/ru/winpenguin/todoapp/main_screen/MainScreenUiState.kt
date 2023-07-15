package ru.winpenguin.todoapp.main_screen

import androidx.annotation.DrawableRes
import ru.winpenguin.todoapp.R

data class MainScreenUiState(
    val todoItems: List<TodoItemUiState> = listOf(),
    val doneItemsCount: Int = 0,
    @DrawableRes val visibilityImageRes: Int = R.drawable.visibility_off
)