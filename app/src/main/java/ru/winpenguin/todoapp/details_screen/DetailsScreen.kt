package ru.winpenguin.todoapp.details_screen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.theme.TodoAppTheme
import java.time.LocalDate


@Composable
fun DetailsComposeScreen(
    id: String?,
    darkTheme: Boolean,
    viewModel: DetailsScreenViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = id) {
        viewModel.updateCurrentItemId(id)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle(DetailsScreenUiState())
    DetailsComposeScreen(
        state = state,
        darkTheme = darkTheme,
        onCloseClicked = navigateBack,
        onSaveClicked = {
            viewModel.saveTodoItem()
            navigateBack.invoke()
        },
        onTextChanged = { text ->
            viewModel.textChanged(text)
        },
        onImportanceChanged = { importance ->
            viewModel.changeImportance(importance)
        },
        onDeadlineCheckedChange = { isChecked ->
            viewModel.changeDeadlineChecked(isChecked)
        },
        onDeadlineConfirmed = { localDate ->
            viewModel.selectDeadline(localDate)
        },
        onCalendarClosed = {
            viewModel.calendarClosed()
        },
        onRemoveClicked = {
            viewModel.removeTodoItem()
            navigateBack.invoke()
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsComposeScreen(
    state: DetailsScreenUiState,
    darkTheme: Boolean,
    onCloseClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onTextChanged: (String) -> Unit,
    onImportanceChanged: (Importance) -> Unit,
    onDeadlineCheckedChange: (Boolean) -> Unit,
    onDeadlineConfirmed: (LocalDate) -> Unit,
    onCalendarClosed: () -> Unit,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = TodoAppTheme.colors.bgPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(
                    state = rememberScrollState(),
                    orientation = Orientation.Vertical,
                    overscrollEffect = null
                )
        ) {
            DetailsToolbar(
                onCloseClicked = onCloseClicked,
                onSaveClicked = onSaveClicked
            )
            TodoDescription(
                description = state.description,
                onTextChanged = onTextChanged
            )
            ImportanceBlock(
                importanceState = state.importance.toUiState(),
                onImportanceClicked = {
                    scope.launch {
                        sheetState.show()
                    }
                },
                modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp)
            )
            Divider(
                color = TodoAppTheme.colors.separator,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            DeadlineSwitch(
                isChecked = state.isDeadlineChecked,
                deadline = state.deadline,
                onCheckedChange = onDeadlineCheckedChange
            )

            Divider(
                color = TodoAppTheme.colors.separator,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(top = 24.dp)
            )
            DeleteButton(onRemoveClicked)
        }

        ImportanceBottomSheet(
            sheetState = sheetState,
            onItemSelected = { importance ->
                onImportanceChanged.invoke(importance)
                scope.launch {
                    sheetState.hide()
                }
            }
        )

        DeadlineCalendar(
            isVisible = state.isCalendarVisible,
            darkTheme = darkTheme,
            onDeadlineConfirmed = onDeadlineConfirmed,
            onCalendarClosed = onCalendarClosed
        )
    }
}

@Composable
private fun TodoDescription(
    description: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = description,
        onValueChange = onTextChanged,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 104.dp)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .shadow(
                elevation = 4.dp,
                clip = false,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = TodoAppTheme.colors.bgSecondary,
                shape = RoundedCornerShape(8.dp)
            ),
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = {
            Text(
                text = stringResource(R.string.what_to_do),
                style = MaterialTheme.typography.bodyLarge,
                color = TodoAppTheme.colors.labelTertiary
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun DeleteButton(
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .clickable { onRemoveClicked.invoke() }
            .padding(12.dp),
        verticalAlignment = CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.delete),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(TodoAppTheme.colors.red)
        )
        Text(
            text = stringResource(R.string.delete),
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = TodoAppTheme.colors.red,
        )
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(
    showBackground = true, name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun DetailScreenPreview() {
    TodoAppTheme {
        DetailsComposeScreen(
            state = DetailsScreenUiState(
                description = "Компоуз - сила",
                deadline = "20 июля 2023",
                isDeadlineChecked = true,
                importance = Importance.HIGH
            ),
            darkTheme = false,
            onSaveClicked = {},
            onCloseClicked = {},
            onDeadlineCheckedChange = {},
            onTextChanged = {},
            onImportanceChanged = {},
            onRemoveClicked = {},
            onDeadlineConfirmed = {},
            onCalendarClosed = {}
        )
    }
}