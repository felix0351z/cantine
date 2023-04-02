package de.juliando.app.android.ui.home


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
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
    categories: List<String>
) {
    var state by remember { mutableStateOf(0) }
    val paddingBetweenItems = 10.dp // Needed, because the standard ScrollableTabRow doesn't have space between the bar items. Because of that a padding is needed,
                                    // which has to be subtracted at the beginning, to start at the correct location

    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        ScrollableTabRow(
            containerColor = CantineTheme.backgroundColor,
            contentColor = CantineTheme.white,
            edgePadding = (-paddingBetweenItems),
            selectedTabIndex = state,
            divider = {}, // No divider and indicator needed for the design
            indicator = {}

        ) {
            categories.forEachIndexed { index, title  ->

                CustomTab(
                    modifier = Modifier
                        .padding(horizontal = paddingBetweenItems),
                    onClick = { state = index }, // Set the current index
                    selected = state == index,
                    title = title,
                    textPadding = 14.dp
                )

            }
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
    Box {
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

}