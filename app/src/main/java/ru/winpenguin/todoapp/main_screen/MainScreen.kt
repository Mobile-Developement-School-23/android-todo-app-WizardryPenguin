package ru.winpenguin.todoapp.main_screen

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.theme.LocalExtendedColors
import ru.winpenguin.todoapp.theme.TodoAppTheme

@Composable
fun MainScreenCompose(
    viewModel: MainScreenViewModel,
    navigateToDetailsScreen: (id: String?) -> Unit,
    navigateToSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle(MainScreenUiState())
    MainScreenCompose(
        state = state,
        onAddItemClicked = { navigateToDetailsScreen.invoke(null) },
        onTodoItemClicked = navigateToDetailsScreen,
        onTodoItemChecked = { id, isChecked ->
            viewModel.changeCheckedState(id, isChecked)
        },
        onVisibilityIconClicked = {
            viewModel.changeItemsVisibility()
        },
        onSettingsClicked = navigateToSettingsScreen,
        modifier = modifier
    )
}

@Composable
fun MainScreenCompose(
    state: MainScreenUiState,
    onAddItemClicked: () -> Unit,
    onTodoItemClicked: (id: String?) -> Unit,
    onTodoItemChecked: (id: String, Boolean) -> Unit,
    onVisibilityIconClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = TodoAppTheme.colors.bgPrimary
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            MainScreenHeader(
                doneItemsCount = state.doneItemsCount,
                visibilityImageRes = state.visibilityImageRes,
                onVisibilityIconClicked = onVisibilityIconClicked,
                onSettingsClicked = onSettingsClicked
            )
            TodoItemsList(
                todoItems = state.todoItems,
                onItemClicked = onTodoItemClicked,
                onItemChecked = onTodoItemChecked
            )
            Spacer(modifier = Modifier.weight(1f))
            AddItemFloatingButton(
                onAddItemClicked = onAddItemClicked,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(onClick = onAddItemClicked)
            )
        }
    }
}

@Composable
private fun TodoItemsList(
    todoItems: List<TodoItemUiState>,
    onItemChecked: (id: String, Boolean) -> Unit,
    onItemClicked: (id: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 16.dp)
            .shadow(
                elevation = 4.dp,
                clip = false,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                shape = RoundedCornerShape(8.dp),
                color = LocalExtendedColors.current.bgSecondary
            )
    ) {
        items(todoItems) { todoItemState ->
            TodoItemElement(
                state = todoItemState,
                onItemChecked = onItemChecked,
                onItemClicked = onItemClicked
            )
        }
    }
}

@Composable
fun AddItemFloatingButton(
    onAddItemClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onAddItemClicked,
        shape = CircleShape,
        containerColor = TodoAppTheme.colors.blue,
        modifier = modifier.padding(end = 16.dp, bottom = 40.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.plus),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@Composable
private fun MainScreenHeader(
    doneItemsCount: Int,
    @DrawableRes visibilityImageRes: Int,
    onVisibilityIconClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = onSettingsClicked,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = null,
                colorFilter = ColorFilter.tint(TodoAppTheme.colors.labelPrimary)
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 60.dp, end = 60.dp, top = 50.dp)
                .align(Alignment.TopStart),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.my_business),
                modifier = Modifier,
                style = MaterialTheme.typography.headlineLarge,
                color = TodoAppTheme.colors.labelPrimary
            )
            Text(
                text = stringResource(id = R.string.done_count, doneItemsCount),
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = TodoAppTheme.colors.labelTertiary
            )
        }
        Image(
            painter = painterResource(id = visibilityImageRes),
            contentDescription = null,
            modifier = Modifier
                .clickable { onVisibilityIconClicked.invoke() }
                .padding(12.dp)
                .align(Alignment.BottomEnd),
            colorFilter = ColorFilter.tint(TodoAppTheme.colors.blue)
        )
    }
}

@Preview(showBackground = true, name = "Light theme")
@Preview(
    showBackground = true,
    name = "Dark theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MainScreenPreview() {
    TodoAppTheme() {
        MainScreenCompose(
            state = MainScreenUiState(
                todoItems = listOf(
                    TodoItemUiState(
                        id = "1",
                        isChecked = false,
                        priorityIconRes = R.drawable.high_priority,
                        text = "Купить что-то 1",
                        additionalText = "7 июня 2023",
                        isCheckboxHighlighted = true
                    ),
                    TodoItemUiState(
                        id = "2",
                        isChecked = true,
                        priorityIconRes = R.drawable.low_priority,
                        text = "Купить что-то 2",
                        additionalText = null,
                        isCheckboxHighlighted = false
                    )
                ),
                doneItemsCount = 4,
                visibilityImageRes = R.drawable.visibility_off
            ),
            onAddItemClicked = {},
            onTodoItemClicked = {},
            onTodoItemChecked = { _, _ -> },
            onSettingsClicked = {},
            onVisibilityIconClicked = {},
        )
    }
}
