package de.juliando.app.android.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.juliando.app.android.R
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.theme.CantineTypography

const val MINIMUM_TAB_HEIGHT = 45

/**
 * Custom tab row for a normal selection
 * If the category is empty, a spacer will be used.
 *
 * @param modifier Modifier of this composable
 * @param onClick Event, if the user clicked a new tab
 * @param categories All items in the row
 *
 **/
@Composable
fun SelectionTab(
    modifier: Modifier = Modifier,
    onClick: (category: String?) -> Unit,
    categories: List<String>,
) {
    var state by remember { mutableStateOf(0) } // Save the current position as int

    if (categories.isNotEmpty()) {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            state = rememberLazyListState()
        ) {

            item {
                CustomTab( // All items tab
                    modifier = Modifier.requiredHeight(MINIMUM_TAB_HEIGHT.dp),
                    onClick = {state = 0; onClick(null) },
                    selected = state == 0,
                    showIcon = false,
                    title = stringResource(R.string.list_selection_all),
                )
            }


            items( // Values
                count = categories.size,
                key = { categories[it] }
            ) {
                val title = categories[it]

                CustomTab(
                    modifier = Modifier.requiredHeight(MINIMUM_TAB_HEIGHT.dp),
                    onClick = {
                        state = it+1 // The state is +1, because the first all tab
                        onClick(title)
                    },
                    selected =  state == it+1 ,
                    showIcon = false,
                    title = title,
                )
            }
        }

    } else {
        Spacer(modifier = Modifier.height(MINIMUM_TAB_HEIGHT.dp))
    }
}


/**
 * Custom tab row for multiple selections
 * If the list of items is empty, a spacer will be used.
 *
 * @param modifier Modifier of this composable
 * @param onClick Event, if the user clicked a new tab
 * @param tags All items in the row
 *
 **/
@Composable
fun MultiSelectionTab(
    modifier: Modifier = Modifier,
    onClick: (tag: String?) -> Unit,
    tags: List<String>,
) {
    val col = remember { mutableStateListOf<Int>() } // Save all current list states in a list

    if (tags.isNotEmpty()) {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            state = rememberLazyListState()
        ) {
            items( // Category values
                count = tags.size,
                key = { tags[it] }
            ) {
                val title = tags[it]

                CustomTab(
                    modifier = Modifier.requiredHeight(MINIMUM_TAB_HEIGHT.dp),
                    onClick = {
                        if (col.contains(it)) col.remove(it) else col.add(it)
                        onClick(title)
                    },
                    selected =  col.contains(it),
                    showIcon = true,
                    title = title,
                )
            }
        }

    } else {
        Spacer(modifier = Modifier.height(MINIMUM_TAB_HEIGHT.dp))
    }
}

/**
 * Custom tab-row bar for category/tag selection
 *
 * @param modifier The Modifier to be applied to this layout tab
 * @param onClick The Event for pressed button
 * @param selected Value if the current bar is selected or not
 * @param title Name of the bar
 * @param shape Corner Shape of the bar
 *
 *
 **/
@Composable
fun CustomTab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selected: Boolean,
    showIcon: Boolean,
    title: String,
    shape: Dp = 14.dp,
) {
    val textColor by animateColorAsState(targetValue = if (selected) CantineTheme.backgroundColor else CantineTheme.white, animationSpec = tween(500))
    val backgroundColor by animateColorAsState(targetValue = if (selected) CantineTheme.white else CantineTheme.surfaceColor, animationSpec = tween(500))

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(shape))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AnimatedVisibility(visible = !selected && showIcon) {
                Icon(
                    modifier = Modifier.padding(vertical = 11.dp).padding(top = 2.dp),
                    painter = painterResource(R.drawable.check),
                    contentDescription = "",
                    tint = textColor
                )
            }

            Text(
                modifier = Modifier.padding(vertical = 13.dp),
                textAlign = TextAlign.Center,
                text = title,
                style = CantineTypography.Bodies.descriptionBody,
                color = textColor
            )

        }

    }

}