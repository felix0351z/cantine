package de.juliando.app.android.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.models.objects.backend.Content
import de.juliando.app.models.objects.ui.Meal

//TODO: Add fallback and error pictures specified for a meal

/**
 * RequestBuilder for the glide image loader in the [Meal] composable.
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
 * Creates a single meal composable from the [Content.Meal] data class.
 *
 * @param modifier Modifier applied to the card layout
 * @param item The meal data
 *
 **/
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Meal(
    modifier: Modifier = Modifier,
    item: Meal,
    innerPadding: Dp = 10.dp,
    heightIn: Pair<Dp, Dp>,
    containerColor: Color = CantineTheme.surfaceColor,
    ) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        onClick = {}
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
                    .padding(horizontal = 5.dp) // Starts 5dp from the left in the row
                    .defaultMinSize(minHeight = heightIn.first),
                verticalArrangement = Arrangement.SpaceBetween // All elements will be arranged with space, looks better
            ) {

                //Tags & the day
                if (item.tags.isNotEmpty() || item.day != null) {
                    Row(
                        modifier = Modifier.height(30.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (item.day != null) {
                            SimpleChip(text = item.day!!)
                        }
                        SimpleChip(text = item.tags[0]) // Only show the first tag for better interaction
                    }
                    Spacer(modifier = Modifier.height(5.dp))// A minimum space has to be between the chips and the title
                }

                Column {
                    Text( // Title
                        text = item.name,
                        style = CantineTypography.Headlines.mealHeadline,
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

                Spacer(modifier = Modifier.height(5.dp)) // A minimum space has to be between the description and the price


                Text( //Price
                    modifier = Modifier.weight(1f, false), // Place at the bottom of the column
                    text = item.toPay,
                    style = CantineTypography.Headlines.mealHeadline,
                    color = CantineColors.primaryColor,
                    maxLines = 1
                )
            }

            GlideImage( // Picture of the meal
                modifier = modifier
                    .weight(2f) // Image takes 2/5 of the width in the row
                    .aspectRatio(1f) // Must be quadratic
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp)), // Clip with a corner shape of 10.dp
                model = item.picture,
                contentDescription = item.name,
                requestBuilderTransform = requestBuilder
            )
        }

    }
}