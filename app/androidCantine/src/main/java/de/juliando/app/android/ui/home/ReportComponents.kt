package de.juliando.app.android.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.bumptech.glide.RequestBuilder

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

import de.juliando.app.android.ui.components.SimpleChip
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTypography

import de.juliando.app.models.objects.backend.Content
import de.juliando.app.models.objects.ui.Report


private const val THUMBNAIL_DIMENSION = 150
private const val ITEMS_TO_PRELOAD = 5
private val THUMBNAIL_SIZE = Size(THUMBNAIL_DIMENSION.toFloat(), THUMBNAIL_DIMENSION.toFloat())


//private fun Content.Report.signature() = MediaStoreSignature(picture)

/**
 * Default request builder to load an report image with Glide
 * This builder is for preloading the data from the list
 * Because of that, this request has to match the request in the
 * actual [ReportCard]
 *
 * @see Content.Report
 **/
private val preloadRequestBuilderTransform = { item: Content.Report, requestBuilder: RequestBuilder<*> ->
    requestBuilder
        .load(item.picture) // Load the image via the url
        //.signature()
        .thumbnail() // Show the picture as thumbnail. Will need less resources and is faster
}

/**
* Default request builder to load an report image with Glide
*
* @see preloadRequestBuilderTransform
**/
@SuppressLint("CheckResult")
private val requestBuilder = { requestBuilder: RequestBuilder<Drawable> ->
    requestBuilder
        .thumbnail()
        //.downsample()
        //.signature()
        .fallback(de.juliando.app.android.R.drawable.default_report) // Default picture if none exists to the item
        .error(de.juliando.app.android.R.drawable.default_report) // Error picture, if an error occurred
        .centerCrop()
}

/**
 * A list with report items
 *
 * @param modifier The modifier to be applied to this list
 * @param state The current state of this lazy list
 * @param cardSize The size of a single report card
 * @param spaceBetween The space between the cards
 * @param items The Reports
 *
 **/
@Composable
fun ReportList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    cardSize: DpSize,
    spaceBetween: Dp,
    items: List<Report>
) {

    LazyRow(
        modifier = modifier.height(cardSize.height),
        state = state, // Remember the current state of the list
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        items(items) {
            ReportCard(
                item = it,
                onClick = {},
                modifier = Modifier.width(cardSize.width)
            )
        }

    }

    // Unused, because for the reports no thumbnails with a specific size are used, may be to change in future
    /*GlideLazyListPreloader(
        state = state, // Remember the current state of the list
        data = items, // Set the current available items
        size = Size(140f, 140f),
        numberOfItemsToPreload = ITEMS_TO_PRELOAD,
        requestBuilderTransform = preloadRequestBuilderTransform
    )*/

}

/**
 * Displays a material 3 card with a report as component
 *
 * @param modifier The modifier to be applied to this card
 * @param item The Report item
 * @param onClick The action for a click event
 *
 **/
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReportCard(
    modifier: Modifier,
    item: Report,
    onClick: () -> Unit
) {
    val horizontalStart = 10.dp
    val cornerShape = 16.dp

    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(cornerShape)
    ) {
        Box {

            GlideImage(
                modifier = Modifier.fillMaxSize(),
                model = item.picture,
                contentDescription = item.title,
                requestBuilderTransform = requestBuilder
            )

            // Used as gradient overlay over the image with headline, date and tag
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CantineColors.blackTransparentGradient)
            ) {

                // Chip to display the tag
                SimpleChip(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(horizontal = horizontalStart, vertical = 10.dp)
                        .height(30.dp),

                    //TODO: Add tag for reports
                    text = "Vegan",
                    chipPadding = 3.dp
                )


                // Title
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart) // Start at the bottom
                        .padding(horizontal = horizontalStart, vertical = 40.dp),

                    text = item.title,
                    style = CantineTypography.Headlines.pictureHeadlineLarge
                )

                // Description, only if a date is available
                if (item.creationTime != null) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomStart) // Start at the bottom
                            .padding(horizontal = horizontalStart, vertical = 15.dp),

                        text = item.creationTime!!,
                        style = CantineTypography.Bodies.pictureBodyLarge
                    )
                }

            }

        }

    }

}

