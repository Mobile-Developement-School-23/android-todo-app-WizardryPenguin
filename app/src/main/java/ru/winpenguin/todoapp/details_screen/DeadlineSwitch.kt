package ru.winpenguin.todoapp.details_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.theme.TodoAppTheme
import ru.winpenguin.todoapp.utils.NoRippleInteractionSource

@Composable
fun DeadlineSwitch(
    isChecked: Boolean,
    deadline: String?,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.heightIn(min = 72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(
                text = stringResource(R.string.deadline),
                style = MaterialTheme.typography.bodyLarge,
                color = TodoAppTheme.colors.labelPrimary,
                modifier = Modifier.padding(start = 16.dp)
            )
            if (deadline != null) {
                Text(
                    text = deadline,
                    style = MaterialTheme.typography.bodySmall,
                    color = TodoAppTheme.colors.blue,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = TodoAppTheme.colors.blue,
                checkedTrackColor = TodoAppTheme.colors.activeSwitchColor,
                uncheckedThumbColor = TodoAppTheme.colors.bgElevated,
                uncheckedTrackColor = TodoAppTheme.colors.grayLight,
                uncheckedBorderColor = Color.Transparent
            ),
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            interactionSource = remember { NoRippleInteractionSource() },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeadlineSwitchPreview() {
    TodoAppTheme {
        DeadlineSwitch(
            isChecked = false,
            deadline = "02 июля 2023",
            onCheckedChange = {},
        )
    }
}