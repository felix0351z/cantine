package de.juliando.app.android.ui.home.shopping_cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.R
import de.juliando.app.android.ui.components.Meal
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.android.utils.DataState
import de.juliando.app.models.objects.ui.Meal


private const val PADDING_SIZE = 20
private const val SPACED_BY_PADDING = 10
private const val MIN_SHEET_HEIGHT = 300

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

        Column(
            modifier = Modifier
                .padding(horizontal = PADDING_SIZE.dp)
                .requiredHeight(MIN_SHEET_HEIGHT.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                val meals by viewModel.meals.collectAsStateWithLifecycle()
                val listState = rememberLazyListState()

                when(meals) {
                    is DataState.Loading -> {
                        // Leave empty, because the items will be saved in the data layer without any time consumption problems
                    }
                    is DataState.Success<*> -> {
                        val list = (meals as DataState.Success<*>).value as List<Meal>
                        if (list.isEmpty()) {
                            // Print the empty view
                            EmptyView()
                        } else {
                            LazyColumn(
                                state = listState,
                                verticalArrangement = Arrangement.spacedBy(SPACED_BY_PADDING.dp)
                            ) {

                                items(
                                    count = list.size,
                                    key = { list[it].id }
                                ) {
                                    Meal(
                                        item = list[it],
                                        onClick = {
                                            TODO()
                                        }
                                    )
                                }





                            }
                        }

                    }
                    is DataState.Error -> {

                    }
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
        Spacer(Modifier.height(30.dp) )
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