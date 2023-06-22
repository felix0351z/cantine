package de.juliando.app.android.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.data.LocalDataStore
import de.juliando.app.models.objects.ui.OrderedMeal


/**
 * RequestBuilder for the glide image loader in the [OrderedMeal] composable.
 * All images will be cropped and overridden with size of 400x400px for less performance issues
 **/
private val requestBuilder = { requestBuilder: RequestBuilder<Drawable> ->
    requestBuilder
        .thumbnail()
        .override(400, 400)
        .fallback(de.juliando.app.android.R.drawable.default_report) // Default picture if none exists to the item
        .error(de.juliando.app.android.R.drawable.default_report) // Error picture, if an error occurred
        .centerCrop()
}


/**
 * Creates a single OrderedMeal composable from the [Meal] data class for the Order.
 *
 * @param modifier Modifier applied to the card layout
 * @param item The OrderedMeal data
 *
 **/
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OrderedMeal(
    modifier: Modifier = Modifier,
    item: OrderedMeal,
    innerPadding: Dp = 10.dp,
    heightIn: Pair<Dp, Dp>,
    containerColor: Color = CantineColors.onSurfaceColor,
    onClick: (OrderedMeal) -> Unit
    ) {

    Card(
        modifier = modifier
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        onClick = {onClick}
    ) {

        Row(
            modifier = Modifier
                .padding(innerPadding)
                .heightIn(min = heightIn.first, max = heightIn.second),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(3f) // Description takes 3/5 of the width in the row
                    .padding(horizontal = 2.dp) // Starts 5dp from the left in the row
                    .defaultMinSize(minHeight = heightIn.first),
                verticalArrangement = Arrangement.SpaceBetween // All elements will be arranged with space, looks better
            ) {
                Column {
                    Text( // Title
                        text = item.name,
                        style = CantineTypography.Headlines.headlineSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text( // Description
                        text = item.description,
                        style = CantineTypography.Bodies.mealBody,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(3.dp)) // A minimum space has to be between the description and the price


                Text( //Price
                    modifier = Modifier.weight(1f, false), // Place at the bottom of the column
                    text = "${try{item.price.toDouble()+item.deposit.toDouble()}catch (e: Exception){item.price}}€",
                    style = CantineTypography.Headlines.headlineSmall,
                    color = CantineColors.primaryColor,
                    maxLines = 1
                )
            }

            GlideImage( // Picture of the meal
                modifier = modifier
                    .weight(1f) // Image takes 1/5 of the width in the row
                    .aspectRatio(1f) // Must be quadratic
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp)), // Clip with a corner shape of 10.dp
                model = "${LocalDataStore.getURL()}/content/image/${item.picture}", // URL from the picture
                contentDescription = item.name,
                requestBuilderTransform = requestBuilder
            )
        }

    }
}