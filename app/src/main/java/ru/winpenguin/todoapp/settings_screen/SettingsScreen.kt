package ru.winpenguin.todoapp.settings_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.theme.ThemeType
import ru.winpenguin.todoapp.theme.ThemeType.DARK
import ru.winpenguin.todoapp.theme.ThemeType.LIGHT
import ru.winpenguin.todoapp.theme.ThemeType.SYSTEM
import ru.winpenguin.todoapp.theme.TodoAppTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themeType by viewModel.selectedThemeType.collectAsStateWithLifecycle(SYSTEM)

    SettingsScreen(
        selectedThemeType = themeType,
        onThemeSelected = { newThemeType ->
            viewModel.selectThemeType(newThemeType)
        },
        onCloseClicked = onCloseClicked,
        modifier = modifier
    )
}

@Composable
fun SettingsScreen(
    selectedThemeType: ThemeType,
    onThemeSelected: (ThemeType) -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = TodoAppTheme.colors.bgPrimary
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingsToolbar(onCloseClicked = onCloseClicked)

            ThemeType.values().forEach { themeType ->
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (themeType) {
                            LIGHT -> stringResource(R.string.always_light)
                            DARK -> stringResource(R.string.always_dark)
                            SYSTEM -> stringResource(R.string.system)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = TodoAppTheme.colors.labelPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    RadioButton(
                        selected = themeType == selectedThemeType,
                        onClick = {
                            onThemeSelected.invoke(themeType)
                        },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsToolbar(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .heightIn(min = 56.dp)
            .fillMaxWidth()
    ) {
        IconButton(onClick = onCloseClicked) {
            Image(
                painter = painterResource(R.drawable.close),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                colorFilter = ColorFilter.tint(TodoAppTheme.colors.labelPrimary)
            )
        }
        Text(
            text = stringResource(R.string.settings),
            color = TodoAppTheme.colors.labelPrimary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    TodoAppTheme {
        SettingsScreen(
            selectedThemeType = LIGHT,
            onThemeSelected = {},
            onCloseClicked = {}
        )
    }
}

