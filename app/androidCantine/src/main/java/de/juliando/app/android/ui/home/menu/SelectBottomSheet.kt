package de.juliando.app.android.ui.home.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.juliando.app.android.R
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.android.ui.theme.CantineTypography.Bodies.bodySmall
import de.juliando.app.android.ui.theme.CantineTypography.Bodies.bodySmallSelected
import de.juliando.app.android.ui.theme.CantineTypography.Bodies.primaryButton
import de.juliando.app.models.objects.ui.Meal
import de.juliando.app.utils.toCurrencyString

private const val SELECTION_LIST_SPACE = 5
private const val BUTTON_HEIGHT = 55
private const val PADDING_SIZE = 20 // Horizontal and bottom


@Composable
fun SelectionList(
    meal: Meal
) {
    val states = remember {
        mutableStateMapOf<Int, String>().also {
            // Initialize with the first value of all groups
            meal.selections.forEachIndexed { index, selectionGroup ->  it[index] = selectionGroup.elements[0].name }
        }
    }

    fun isSelected(index: Int, name: String) = name == (states[index] ?: "")

    meal.selections.forEachIndexed { groupIndex, it ->
        Column(
            verticalArrangement = Arrangement.spacedBy(SELECTION_LIST_SPACE.dp)
        ) {
            // Headline of the selection
            Text(
                text = it.name,
                style = CantineTypography.Headlines.headlineLarge
            )

            // Sub-elements of the selection
            it.elements.forEach {
                val isSelected = isSelected(groupIndex, it.name)

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = CantineTheme.primaryColor,
                            unselectedColor = CantineTheme.grey1
                        ),
                        onClick = { states[groupIndex] = it.name }
                    )
                    Text(
                        text = it.name,
                        style = if (isSelected) bodySmallSelected else bodySmall
                    )
                    Spacer(modifier = Modifier.width(SELECTION_LIST_SPACE.dp))

                    if (isSelected) {
                        Text(
                            text = "+ ${toCurrencyString(it.price)}",
                            style = bodySmall
                        )
                    }
                }


            }
        }
    }




}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBottomSheet(
    onDismiss: () -> Unit,
    meal: Meal
) {
    // Remember the current sheet state for the bottom sheet layout
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var amountState by remember { mutableStateOf(1) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CantineColors.backgroundColor,
        sheetState = modalSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = PADDING_SIZE.dp),
            verticalArrangement = Arrangement.spacedBy(PADDING_SIZE.dp),
        ) {
            SelectionList(meal = meal)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                /*
                * Represent the amount bar with the add/remove buttons
                */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CounterBox(imageVector = Icons.Filled.Add, contentDescription = "Add", onClick = {
                        if (amountState < 5) amountState++
                    })

                    Text(text = "$amountState", fontSize = 30.sp)

                    CounterBox(imageVector = Icons.Filled.Remove, contentDescription = "Remove", onClick = {
                        if (amountState > 1) amountState--
                    })
                }

                /*
                * Buy button
                */
                Button(
                    modifier = Modifier.height(BUTTON_HEIGHT.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = CantineColors.primaryColor, contentColor = CantineColors.black),
                    onClick = { /*TODO*/ },
                ) {
                    Text(text = stringResource(R.string.add_to_shopping_cart), style = primaryButton)
                    Spacer(modifier = Modifier.size(10.dp))
                    Icon(Icons.Outlined.ShoppingCart, stringResource(R.string.add_to_shopping_cart))
                }
                

            }

            Spacer(modifier = Modifier.height(PADDING_SIZE.dp))

        }
    }
}

@Composable
fun CounterBox(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(BUTTON_HEIGHT.dp, BUTTON_HEIGHT.dp)
            .clip(CircleShape)
            .background(CantineColors.onSurfaceColor)
            .clickable { onClick() },
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}


