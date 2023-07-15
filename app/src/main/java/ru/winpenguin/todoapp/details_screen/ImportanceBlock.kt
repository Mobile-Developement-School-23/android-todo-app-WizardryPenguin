package ru.winpenguin.todoapp.details_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.theme.TodoAppTheme

@Composable
fun ImportanceBlock(
    importanceState: ImportanceUiState,
    onImportanceClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .clickable { onImportanceClicked.invoke() }
        ) {
            Text(
                text = stringResource(R.string.importance),
                style = MaterialTheme.typography.bodyLarge,
                color = TodoAppTheme.colors.labelPrimary
            )
            Text(
                text = importanceState.name,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = importanceState.color
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun ImportanceBlockPreview() {
    TodoAppTheme {
        ImportanceBlock(
            importanceState = Importance.LOW.toUiState(),
            onImportanceClicked = {}
        )
    }
}