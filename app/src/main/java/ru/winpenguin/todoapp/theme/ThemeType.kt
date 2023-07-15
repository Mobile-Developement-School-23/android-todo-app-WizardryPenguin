package ru.winpenguin.todoapp.theme

import java.util.Locale

enum class ThemeType {
    LIGHT, DARK, SYSTEM;

    companion object {
        fun fromString(name: String?): ThemeType {
            return when (name?.uppercase(Locale.getDefault())) {
                LIGHT.name -> LIGHT
                DARK.name -> DARK
                SYSTEM.name -> SYSTEM
                else -> SYSTEM
            }
        }
    }
}