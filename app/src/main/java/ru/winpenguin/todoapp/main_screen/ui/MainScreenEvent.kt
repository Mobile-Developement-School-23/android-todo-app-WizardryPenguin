package ru.winpenguin.todoapp.main_screen.ui

import androidx.annotation.StringRes

sealed class MainScreenEvent {
    data class ShowMessage(@StringRes val message: Int) : MainScreenEvent()
}