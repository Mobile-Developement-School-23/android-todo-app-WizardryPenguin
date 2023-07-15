package ru.winpenguin.todoapp.details_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.theme.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportanceBottomSheet(
    sheetState: SheetState,
    onItemSelected: (Importance) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
            containerColor = TodoAppTheme.colors.bgPrimary,
            windowInsets = WindowInsets(bottom = 56.dp),
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Importance.values().forEach { importance ->
                    val uiState = importance.toUiState()
                    Text(
                        text = uiState.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = uiState.color,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemSelected(importance) }
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ImportanceBottomSheetPreview() {
    TodoAppTheme {
        ImportanceBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onItemSelected = {}
        )
    }
}