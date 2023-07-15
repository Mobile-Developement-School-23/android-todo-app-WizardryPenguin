package ru.winpenguin.todoapp.theme

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {

    private val _selectedThemeType = MutableStateFlow(getSelectedThemeType())
    val selectedThemeTypeFlow: Flow<ThemeType> = _selectedThemeType.asStateFlow()

    fun selectThemeType(themeType: ThemeType) {
        sharedPreferences.edit {
            putString(THEME_TYPE_KEY, themeType.name)
        }
        _selectedThemeType.value = themeType
    }

    fun getSelectedThemeType(): ThemeType {
        val themeName = sharedPreferences.getString(THEME_TYPE_KEY, ThemeType.SYSTEM.name)
        return ThemeType.fromString(themeName)
    }

    companion object {
        private const val THEME_TYPE_KEY = "THEME_TYPE_KEY"
    }
}