package de.juliando.app.android.ui.home.shopping_cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.R
import de.juliando.app.android.ui.components.ErrorMessage
import de.juliando.app.android.ui.components.OrderedMeal
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.android.utils.DataState

private const val PADDING_SIZE = 20
private const val SPACED_BY_PADDING = 10
private const val MIN_SHEET_HEIGHT = 200

private const val BUTTON_HEIGHT = 55

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    onDismiss: () -> Unit,
    viewModel: ShoppingCartViewModel
) {
    // Remember the current sheet state for the bottom sheet layout
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CantineColors.backgroundColor,
        sheetState = modalSheetState,
        dragHandle = {
            // Visual drag handle marker at the top
            BottomSheetDefaults.DragHandle()
        }
    ) {
        // The shopping cart view of the bottom sheet:

            Box(
                Modifier
                    .defaultMinSize(minHeight = MIN_SHEET_HEIGHT.dp)
                    .fillMaxWidth()
                    .padding(PADDING_SIZE.dp)
            ) {
                val meals by viewModel.meals.collectAsStateWithLifecycle()
                when(meals) {
                    is DataState.Loading -> {
                        // Leave empty, because the items will be saved in the data layer without any time consumption problems
                    }
                    is DataState.Success<*> -> {
                        val values = (meals as DataState.Success<*>).value as ShoppingCartViewModel.ShoppingCartItems

                        if (values.items.isEmpty()) {
                            // Print the empty view
                            EmptyView()
                        } else {
                            View(items = values,
                                onPaymentClick = viewModel::onPaymentClick,
                                onItemClick = viewModel::onDeleteClick
                            )
                        }
                    }
                    is DataState.Error -> {
                        // Show a simple error message
                        ErrorMessage()
                    }
                }
            }

    }
}

@Composable
private fun View(
    items: ShoppingCartViewModel.ShoppingCartItems,
    onPaymentClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(SPACED_BY_PADDING.dp),
    ) {
        items(items.items) {
            OrderedMeal(
                item = it,
                onClick = { onItemClick(it.id) }
            )
        }

        item {
            Spacer(Modifier.height(40.dp))
        }

        item {

            Box(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = items.amount
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .height(BUTTON_HEIGHT.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = CantineColors.primaryColor, contentColor = CantineColors.black),
                    onClick = onPaymentClick,
                ) {
                    Text(text = stringResource(R.string.shopping_cart_pay), style = CantineTypography.Bodies.primaryButton)
                    Spacer(modifier = Modifier.size(10.dp))
                    Icon(Icons.Outlined.AttachMoney, stringResource(R.string.shopping_cart_pay))
                }
            }
        }


    }
}

@Composable
private fun BoxScope.EmptyView() {
    Column(
        modifier = Modifier.align(Alignment.TopCenter),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.shopping_cart),
            contentDescription = "Shopping cart",
            modifier = Modifier.size(90.dp)
        )
        Text(
            text = stringResource(R.string.shopping_cart_empty),
            textAlign = TextAlign.Center,
            style = CantineTypography.Bodies.emptyViewStyle
        )

    }
}