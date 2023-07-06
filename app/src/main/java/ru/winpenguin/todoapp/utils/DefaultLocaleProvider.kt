package ru.winpenguin.todoapp.utils

import java.util.Locale
import javax.inject.Inject

class DefaultLocaleProvider @Inject constructor() : LocaleProvider {
    override fun locale(): () -> Locale = { Locale.getDefault() }
}