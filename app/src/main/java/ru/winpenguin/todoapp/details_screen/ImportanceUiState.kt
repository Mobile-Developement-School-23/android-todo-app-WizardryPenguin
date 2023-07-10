package ru.winpenguin.todoapp.details_screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.theme.TodoAppTheme

data class ImportanceUiState(
    val name: String,
    val color: Color
)

@Composable
fun Importance.toUiState(): ImportanceUiState {
    return when (this) {
        Importance.LOW -> ImportanceUiState(
            name = stringResource(R.string.none),
            color = TodoAppTheme.colors.labelTertiary
        )

        Importance.NORMAL -> ImportanceUiState(
            name = stringResource(R.string.low),
            color = TodoAppTheme.colors.labelPrimary
        )

        Importance.HIGH -> ImportanceUiState(
            name = stringResource(R.string.high),
            color = TodoAppTheme.colors.red
        )
    }
}
