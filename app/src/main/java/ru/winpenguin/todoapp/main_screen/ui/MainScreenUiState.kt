package ru.winpenguin.todoapp.main_screen.ui

data class MainScreenUiState(
    val todoItems: List<TodoItemUiState> = listOf(),
    val doneItemsCount: Int = 0,
)
