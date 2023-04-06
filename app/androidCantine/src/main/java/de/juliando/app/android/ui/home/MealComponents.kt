package de.juliando.app.android.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.android.ui.utils.DataState

const val MINIMUM_TAB_HEIGHT = 45

@Composable
fun MealTabs(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    categories: DataState,
    textPadding: Dp = 14.dp
) {
    var state by remember { mutableStateOf(0) }

    when(categories) {
        is DataState.Success<*> -> {
            LazyRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                state = rememberLazyListState()
            ) {
                val values = categories.value as List<String>

                items(
                    count = values.size,
                    key = { values[it] }
                ) {
                    CustomTab(
                        modifier = Modifier.requiredHeight(MINIMUM_TAB_HEIGHT.dp),
                        onClick = { state = it; onClick() },
                        selected = state == it,
                        title = values[it],
                        textPadding = textPadding
                    )
                }
            }

        }
        else -> {
            Spacer(modifier = Modifier.height(MINIMUM_TAB_HEIGHT.dp))
        }
    }
}

/**
 * Custom tab-row bar for category selection
 *
 * @param modifier The Modifier to be applied to this layout tab
 * @param onClick The Event for pressed button
 * @param selected Value if the current bar is selected or not
 * @param title Name of the bar
 * @param shape Corner Shape of the bar
 * @param textPadding Padding of the text inside the bar
 *
 *
 **/
@Composable
fun CustomTab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selected: Boolean,
    title: String,
    shape: Dp = 14.dp,
    textPadding: Dp
) {
    val textColor by animateColorAsState(targetValue = if (selected) CantineTheme.backgroundColor else CantineTheme.white, animationSpec = tween(500))
    val backgroundColor by animateColorAsState(targetValue = if (selected) CantineTheme.white else CantineTheme.surfaceColor, animationSpec = tween(500))
        
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(shape))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.padding(textPadding),
            textAlign = TextAlign.Center,
            text = title,
            style = CantineTypography.Bodies.descriptionBody,
            color = textColor
        )

    }
}