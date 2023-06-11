package ru.winpenguin.todoapp.main_screen.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class UiTodoItem(
    val id: String,
    val isChecked: Boolean,
    @ColorRes val checkBoxColorRes: Int,
    val text: String,
    @ColorRes val textColorRes: Int,
    val isStrikedThrough: Boolean,
    @DrawableRes val leadIconRes: Int?,
    val additionalText: String?,
)
