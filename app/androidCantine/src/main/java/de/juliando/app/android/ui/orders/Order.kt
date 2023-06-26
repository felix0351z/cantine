package de.juliando.app.android.ui.orders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.juliando.app.android.ui.components.OrderedMeal
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.models.objects.ui.Order
import de.juliando.app.utils.format
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Order(
    item: Order,
    modifier: Modifier = Modifier,
    containerColor: Color = CantineTheme.surfaceColor,
    innerPadding: Dp = 10.dp,
    cornerShape: Int,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(cornerShape.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = containerColor),
        ) {

            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .heightIn(min = 130.dp, max = 1000.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .defaultMinSize(minHeight = 130.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {

                    Text(  // Title
                        text = toFormattedOrderTime(item.orderTime),
                        style = CantineTypography.Headlines.headlineMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .defaultMinSize(minHeight = 130.dp)
                            .heightIn(min=130.dp, max=200.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ){
                        items(
                            count = item.meals.size,
                            key = { item.meals[it].name },
                        ) {
                            OrderedMeal(
                                modifier = Modifier.clip(RoundedCornerShape(CORNER_SHAPE.dp)),
                                heightIn = Pair(100.dp, 110.dp),
                                item = item.meals[it],
                                onClick = onClick
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))


                    Text( // Price
                        modifier = Modifier.weight(1f, false),
                        text = item.toPay,
                        style = CantineTypography.Headlines.headlineMedium,
                        color = CantineColors.primaryColor,
                        maxLines = 1,
                    )

                }
            }
        }
    }
}

fun toFormattedOrderTime(orderTime: Instant): String {
    return orderTime.toLocalDateTime(TimeZone.currentSystemDefault()).format("'Bestellung von' EEEE, dd.MM.yy, HH:mm")
}