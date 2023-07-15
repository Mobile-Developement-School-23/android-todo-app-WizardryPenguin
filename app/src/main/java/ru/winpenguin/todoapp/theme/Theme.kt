package ru.winpenguin.todoapp.theme

import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    secondary = DarkGrey,
    tertiary = Pink80,
    background = DarkBgPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = LightBlue,
    secondary = LightGrey,
    tertiary = Pink40,
    background = LightBgPrimary
)

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    Log.d("TAG", "TodoAppTheme: darkTheme: $darkTheme")
    val extendedColors = ExtendedColors(
        labelTertiary = if (darkTheme) DarkLabelTertiary else LightLabelTertiary,
        labelPrimary = if (darkTheme) DarkLabelPrimary else LightLabelPrimary,
        green = if (darkTheme) DarkGreen else LightGreen,
        red = if (darkTheme) DarkRed else LightRed,
        labelDisable = if (darkTheme) DarkLabelDisable else LightLabelDisable,
        separator = if (darkTheme) DarkSeparator else LightSeparator,
        labelSecondary = if (darkTheme) DarkLabelSecondary else LightLabelSecondary,
        blue = if (darkTheme) DarkBlue else LightBlue,
        gray = if (darkTheme) DarkGrey else LightGrey,
        grayLight = if (darkTheme) DarkGreyLight else LightGreyLight,
        bgPrimary = if (darkTheme) DarkBgPrimary else LightBgPrimary,
        bgSecondary = if (darkTheme) DarkBgSecondary else LightBgSecondary,
        bgElevated = if (darkTheme) DarkBgElevated else LightBgElevated,
        activeSwitchColor = if (darkTheme) CheckedSwitchColor else CheckedSwitchColor,
        overlay = if (darkTheme) DarkOverlay else LightOverlay,
    )
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme

    DisposableEffect(systemUiController, useDarkIcons, isSystemInDarkTheme) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object TodoAppTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}