package ru.winpenguin.todoapp.main_screen.ui

import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class TodoItemUiState(
    val id: String,
    val isChecked: Boolean,
    @ColorRes val checkBoxColorRes: Int,
    val text: String,
    @AttrRes val textColorAttr: Int,
    val isStrikedThrough: Boolean,
    @DrawableRes val priorityIconRes: Int?,
    val additionalText: String?,
    @DrawableRes val backgroundRes: Int
)
