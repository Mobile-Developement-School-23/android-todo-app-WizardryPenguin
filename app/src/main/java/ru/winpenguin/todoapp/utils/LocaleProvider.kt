package ru.winpenguin.todoapp.utils

import java.util.Locale

interface LocaleProvider {
    fun locale(): () -> Locale
}