package de.juliando.app.android.ui.home


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun MealTabs(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    categories: DataState
) {
    var state by remember { mutableStateOf(0) }

    when(categories) {


        is DataState.Success<*> -> {
            LazyRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(13.dp),
                state = rememberLazyListState()
            ) {
                val categories = categories.value as List<String>

                items(
                    count = categories.size,
                    key = { categories[it] }
                ) {
                    CustomTab(
                        onClick = { state = it; onClick() },
                        selected = state == it,
                        title = categories[it],
                        textPadding = 14.dp
                    )
                }
            }

        }
        is DataState.Loading -> {

        }
        is DataState.Error -> {

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