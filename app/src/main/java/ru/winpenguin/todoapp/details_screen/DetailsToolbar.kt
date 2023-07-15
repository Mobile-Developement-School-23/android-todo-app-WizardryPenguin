package ru.winpenguin.todoapp.details_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.theme.TodoAppTheme

@Composable
fun DetailsToolbar(
    onCloseClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCloseClicked) {
            Image(
                painter = painterResource(R.drawable.close),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                colorFilter = ColorFilter.tint(TodoAppTheme.colors.labelPrimary)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.save).toUpperCase(Locale.current),
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                .clickable { onSaveClicked.invoke() }
                .padding(horizontal = 16.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            color = TodoAppTheme.colors.blue
        )
    }
}

@Preview
@Composable
fun DetailComposeToolbarPreview() {
    TodoAppTheme() {
        DetailsToolbar(
            onCloseClicked = { /*TODO*/ },
            onSaveClicked = { /*TODO*/ },
            Modifier.background(TodoAppTheme.colors.bgPrimary)
        )
    }
}
