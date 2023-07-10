package ru.winpenguin.todoapp.details_screen

import android.os.Build
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import ru.winpenguin.todoapp.theme.DarkBlue
import ru.winpenguin.todoapp.theme.LightBlue
import ru.winpenguin.todoapp.theme.Pink80
import ru.winpenguin.todoapp.theme.PurpleGrey80
import ru.winpenguin.todoapp.theme.Typography
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlineCalendar(
    isVisible: Boolean,
    darkTheme: Boolean,
    onDeadlineConfirmed: (LocalDate) -> Unit,
    onCalendarClosed: () -> Unit
) {
    val calendarState = rememberUseCaseState(
        visible = false,
        onCloseRequest = {
            onCalendarClosed.invoke()
        }
    )

    DisposableEffect(key1 = isVisible) {
        if (isVisible) {
            calendarState.show()
        }
        onDispose {}
    }

    CalendarTheme(darkTheme = darkTheme) {
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
                boundary = LocalDate.now()..LocalDate.now().plusYears(20)
            ),
            selection = CalendarSelection.Date { deadline ->
                Log.d("TAG", "selection")
                onDeadlineConfirmed(deadline)
            },
        )
    }
}


@Preview
@Composable
fun CalendarSamplePreview() {
    CalendarTheme(darkTheme = false) {
        DeadlineCalendar(
            isVisible = true,
            darkTheme = false,
            onDeadlineConfirmed = {},
            onCalendarClosed = {}
        )
    }
}

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    )

private val LightColorScheme = lightColorScheme(
    primary = LightBlue,
    secondary = Color.Green,
    tertiary = Color.Transparent,
    primaryContainer = Color.White,
    secondaryContainer = LightBlue
)

@Composable
fun CalendarTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}