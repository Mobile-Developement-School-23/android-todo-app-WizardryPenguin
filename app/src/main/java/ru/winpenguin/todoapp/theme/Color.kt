package ru.winpenguin.todoapp.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val labelTertiary: Color,
    val labelPrimary: Color,
    val green: Color,
    val red: Color,
    val labelDisable: Color,
    val separator: Color,
    val labelSecondary: Color,
    val blue: Color,
    val gray: Color,
    val grayLight: Color,
    val bgPrimary: Color,
    val bgSecondary: Color,
    val bgElevated: Color,
    val activeSwitchColor: Color,
    val overlay: Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        labelTertiary = Color.Unspecified,
        labelPrimary = Color.Unspecified,
        green = Color.Unspecified,
        red = Color.Unspecified,
        labelDisable = Color.Unspecified,
        separator = Color.Unspecified,
        labelSecondary = Color.Unspecified,
        blue = Color.Unspecified,
        gray = Color.Unspecified,
        grayLight = Color.Unspecified,
        bgPrimary = Color.Unspecified,
        bgSecondary = Color.Unspecified,
        bgElevated = Color.Unspecified,
        activeSwitchColor = Color.Unspecified,
        overlay = Color.Unspecified
    )
}

val LightLabelTertiary = Color(0x4D000000)
val DarkLabelTertiary = Color(0x66FFFFFF)
val LightLabelPrimary = Color(0xFF000000)
val DarkLabelPrimary = Color(0xFFFFFFFF)
val LightGreen = Color(0xFF34C759)
val DarkGreen = Color(0xFF32D74B)
val LightRed = Color(0xFFFF3B30)
val DarkRed = Color(0xFFFF453A)
val LightLabelDisable = Color(0x26000000)
val DarkLabelDisable = Color(0x26FFFFFF)
val LightSeparator = Color(0x33000000)
val DarkSeparator = Color(0x33FFFFFF)
val LightLabelSecondary = Color(0x99000000)
val DarkLabelSecondary = Color(0x99FFFFFF)
val LightBlue = Color(0xFF007AFF)
val DarkBlue = Color(0xFF0A84FF)
val LightGrey = Color(0xFF8E8E93)
val DarkGrey = Color(0xFF8E8E93)
val LightGreyLight = Color(0xFFD1D1D6)
val DarkGreyLight = Color(0xFFD1D1D6)
val LightBgPrimary = Color(0xFFF7F6F2)
val DarkBgPrimary = Color(0xFF161618)
val LightBgSecondary = Color(0xFFFFFFFF)
val DarkBgSecondary = Color(0xFF252528)
val LightBgElevated = Color(0xFFFFFFFF)
val DarkBgElevated = Color(0xFF3C3C3F)

val CheckedSwitchColor = Color(0xFF91B9E4)
val LightOverlay = Color(0x0F000000)
val DarkOverlay = Color(0x52000000)

val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Pink40 = Color(0xFF7D5260)

