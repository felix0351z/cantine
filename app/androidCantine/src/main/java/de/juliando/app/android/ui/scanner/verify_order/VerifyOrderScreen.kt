package de.juliando.app.android.ui.scanner.verify_order

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.R
import de.juliando.app.android.ui.components.OrderedMeal
import de.juliando.app.android.ui.components.ShimmerItem
import de.juliando.app.android.ui.orders.CORNER_SHAPE
import de.juliando.app.android.ui.orders.toFormattedOrderTime
import de.juliando.app.android.ui.orders.views.*
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.android.utils.DataState
import de.juliando.app.models.objects.ui.Order

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOrderScreen(
    viewModel: VerifyOrderViewModel,
    onBackPressed: () -> Unit
) {
    val order by viewModel.order.collectAsStateWithLifecycle()

    BackHandler(enabled = true, onBack = onBackPressed)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "ArrowBack", tint = Color.White)
                    }
                }
            )
        }
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = rememberLazyListState(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(order) {
                is DataState.Loading -> {

                    item {
                        /*
                        * Loading animation for the title element
                        */
                        ShimmerItem(
                            Modifier
                                .size(width = LOADING_TITLE_WIDTH, height = LOADING_TITLE_HEIGHT)
                                .clip(LOADING_TITLE_CLIP)
                        )
                    }

                    item {
                        /*
                        * Loading animation
                        */
                        Column(
                            verticalArrangement = Arrangement.spacedBy(PADDING /2)
                        ) {
                            for (i in 1..10) {
                                ShimmerItem(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(LOADING_BODY_HEIGHT)
                                        .clip(LOADING_BODY_CLIP)
                                )
                            }
                            ShimmerItem(
                                Modifier
                                    .size(width = LOADING_TITLE_WIDTH, height = LOADING_BODY_HEIGHT)
                                    .clip(LOADING_BODY_CLIP)
                            )
                        }
                    }

                }
                is DataState.Success<*> -> {
                    val value: Order = (order as DataState.Success<*>).value as Order

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    item {
                            Text( // Title
                                modifier = Modifier,
                                text = toFormattedOrderTime(value.orderTime),
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center
                            )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    items(value.meals) {
                        OrderedMeal(
                            modifier = Modifier
                                .clip(RoundedCornerShape(CORNER_SHAPE.dp)),
                            padding = 6.dp,
                            heightIn = Pair(100.dp, 110.dp),
                            item = it,
                            onClick = {},
                            onLongClick = {}
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    item {
                        Button(
                            onClick = onBackPressed,
                            modifier = Modifier
                                .size(width = 200.dp, height = 50.dp),
                            shape = RoundedCornerShape(30)
                        ) {
                            Text(
                                text = stringResource(R.string.done),
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                is DataState.Error -> {
                    // Shows the error message if sth goes wrong
                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                        Icon(
                            Icons.Outlined.Cancel,
                            contentDescription = "Error",
                            modifier = Modifier.size(140.dp),
                            CantineColors.white50Transparent
                        )
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = stringResource(R.string.error_occurred),
                            textAlign = TextAlign.Center,
                            style = CantineTypography.Headlines.headlineMedium,
                            color = CantineColors.white50Transparent
                        )
                    }
                }
            }
        }
    }
}