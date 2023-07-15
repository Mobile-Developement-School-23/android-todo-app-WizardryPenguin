package ru.winpenguin.todoapp.settings_screen

import androidx.lifecycle.ViewModel
import ru.winpenguin.todoapp.theme.ThemeRepository
import ru.winpenguin.todoapp.theme.ThemeType
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    val selectedThemeType = themeRepository.selectedThemeTypeFlow

    fun selectThemeType(themeType: ThemeType) {
        themeRepository.selectThemeType(themeType)
    }
}