package ru.winpenguin.todoapp.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.theme.TodoAppTheme

@Composable
fun TodoItemElement(
    state: TodoItemUiState,
    onItemChecked: (id: String, Boolean) -> Unit,
    onItemClicked: (id: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked.invoke(state.id)
            }
    ) {
        Checkbox(
            checked = state.isChecked,
            onCheckedChange = { isChecked ->
                onItemChecked(state.id, isChecked)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = TodoAppTheme.colors.green,
                uncheckedColor = if (state.isCheckboxHighlighted) TodoAppTheme.colors.red else TodoAppTheme.colors.labelDisable
            )
        )
        if (state.priorityIconRes != null) {
            Image(
                painter = painterResource(id = state.priorityIconRes),
                contentDescription = null,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
        TextsBlock(
            text = state.text,
            isTextStrikeThrough = state.isChecked,
            textHasTertiaryColor = state.isChecked,
            additionalText = state.additionalText,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.info_outline),
            contentDescription = null,
            modifier = Modifier.padding(14.dp),
            colorFilter = ColorFilter.tint(TodoAppTheme.colors.labelTertiary)
        )
    }
}

@Composable
private fun TextsBlock(
    text: String,
    isTextStrikeThrough: Boolean,
    textHasTertiaryColor: Boolean,
    additionalText: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (isTextStrikeThrough) TextDecoration.LineThrough else null
            ),
            color = if (textHasTertiaryColor) TodoAppTheme.colors.labelTertiary else TodoAppTheme.colors.labelPrimary
        )
        if (additionalText != null) {
            Text(
                text = additionalText,
                style = MaterialTheme.typography.bodySmall,
                color = TodoAppTheme.colors.labelTertiary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoItemElementPreview() {
    TodoAppTheme {
        TodoItemElement(
            state = TodoItemUiState(
                id = "1",
                isChecked = true,
                priorityIconRes = R.drawable.high_priority,
                text = "Купить что-то",
                additionalText = "7 июня 2023",
                isCheckboxHighlighted = true
            ),
            onItemChecked = { _, _ -> },
            onItemClicked = {}
        )
    }
}

