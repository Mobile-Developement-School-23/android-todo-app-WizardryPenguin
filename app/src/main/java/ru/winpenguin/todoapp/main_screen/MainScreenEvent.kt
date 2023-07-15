package ru.winpenguin.todoapp.main_screen

import androidx.annotation.StringRes

sealed class MainScreenEvent {
    data class ShowMessage(@StringRes val message: Int) : MainScreenEvent()
}