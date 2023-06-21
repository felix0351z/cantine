package de.juliando.app.android.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.R
import de.juliando.app.android.ui.components.ShimmerItem
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.utils.ViewState

const val SPACE_TOP = 50
const val SPACED_BY = 20

const val CORNER_SHAPE = 16

@Composable
fun OrderScreen(
    viewModel: OrderViewModel
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .background(CantineColors.backgroundColor)
            .fillMaxSize()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp),
        verticalArrangement = Arrangement.spacedBy(de.juliando.app.android.ui.home.SPACED_BY.dp),
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

        when(viewState) {
            is ViewState.Success -> {
                items(
                    count = orders.size,
                    key = { orders[it].id }
                ) {
                    Order(
                        modifier = Modifier.clip(RoundedCornerShape(de.juliando.app.android.ui.home.CORNER_SHAPE.dp)),
                        item = orders[it],
                        onClick = {},
                        onLongClick = {},
                        cornerShape = CORNER_SHAPE
                    )
                }
            }
            is ViewState.Loading -> {
                items(count = 5) {
                    ShimmerItem(modifier = Modifier
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