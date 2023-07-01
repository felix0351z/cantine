package de.juliando.app.android.ui.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import de.juliando.app.android.R
import de.juliando.app.android.ui.components.ShimmerItem
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.android.utils.ViewState

const val SPACE_TOP = 50
const val SPACED_BY = 20

const val CORNER_SHAPE = 16

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    onOrderClick: (String) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsState()
    val isDeleteDialogOpened = remember { mutableStateOf<String?>(null) }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    if (isDeleteDialogOpened.value != null) {
        AlertDialog(
            onDismissRequest = { isDeleteDialogOpened.value = null },
            containerColor = CantineColors.backgroundColor,
            titleContentColor = CantineColors.white,
            textContentColor = CantineTheme.grey1,
            title = { Text(text = stringResource(R.string.order_cancel_title)) },
            text = { Text(stringResource(R.string.order_cancel_description)) },
            confirmButton = {
                Button(
                    colors = CantineColors.primaryButtonColors(),
                    onClick = {
                        // Delete the meal
                        viewModel.onDeleteClick(isDeleteDialogOpened.value!!)
                        isDeleteDialogOpened.value = null
                    }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    colors = CantineColors.secondaryButtonColors(),
                    onClick = {
                        isDeleteDialogOpened.value = null
                    }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.refresh() },
        indicator = {state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                backgroundColor = CantineColors.surfaceColor,
                contentColor = CantineColors.primaryColor
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .background(CantineColors.backgroundColor)
                .fillMaxSize()
                .padding(top = 5.dp, start = 5.dp, end = 5.dp),
            verticalArrangement = Arrangement.spacedBy(SPACED_BY.dp),
            state = rememberLazyListState(),
        ) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(SPACE_TOP.dp))
                    Text(
                        text = stringResource(R.string.orders_title),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(SPACED_BY.dp))
                }
            }

            when (viewState) {
                is ViewState.Success -> {
                    if (orders.isEmpty()) {
                        item {
                            EmptyView()
                        }
                    } else {
                        items(
                            count = orders.size,
                            key = { orders[it].id },
                        ) {
                            Order(
                                modifier = Modifier.clip(RoundedCornerShape(de.juliando.app.android.ui.home.CORNER_SHAPE.dp)),
                                item = orders[it],
                                onClick = { onOrderClick(orders[it].id) },
                                onLongClick = { isDeleteDialogOpened.value = orders[it].id },
                                cornerShape = CORNER_SHAPE
                            )
                        }
                    }
                }
                is ViewState.Loading -> {
                    items(count = 5) {
                        ShimmerItem(
                            modifier = Modifier
                                .clip(RoundedCornerShape(de.juliando.app.android.ui.home.CORNER_SHAPE.dp))
                                .fillMaxWidth()
                                .height(400.dp)
                        )
                    }
                }
                is ViewState.Error -> {
                    TODO()
                }
            }
        }
    }
}

@Composable
fun EmptyView(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(30.dp) )
        Image(
            painter = painterResource(id = R.drawable.fork_and_knife),
            contentDescription = "fork and knife",
            modifier = Modifier.size(140.dp)
        )
        Text(
            text = stringResource(R.string.order_sceen_empty),
            textAlign = TextAlign.Center,
            style = CantineTypography.Bodies.emptyViewStyle,
            fontSize = 20.sp
        )

    }
}
