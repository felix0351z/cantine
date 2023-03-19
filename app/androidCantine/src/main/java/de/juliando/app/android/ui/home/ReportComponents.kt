package de.juliando.app.android.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide

import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideLazyListPreloader
import de.juliando.app.android.CantineTheme

import de.juliando.app.models.objects.Content

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReportList(
    items: List<Content.Report>,
    state: LazyListState,
) {
    val requestManager = Glide.with(LocalContext.current)
    val size = Size(200f, 100f)

    GlideLazyListPreloader(
        state = state,
        data = items,
        size = size,
        numberOfItemsToPreload = 2,
    ) { item, requestBuilder ->
        requestBuilder.load(item.picture)
    }

    LazyColumn(
        state = state
    ) {
        items(items) {
            ReportCard(item = it, requestManager)
        }

    }


    
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReportCard(
    item: Content.Report,
    requestManager: RequestManager,
) {
    Column(
        modifier = Modifier.size(305.dp, 200.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Picture
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            model = item.picture,
            contentDescription = item.title
        ) {
            it
                .error(item.picture)
                .thumbnail(
                    requestManager
                        .asDrawable()
                        .load(item.picture)
                        .override(200, 100)
                )
                .fitCenter()
        }
        // Title
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge
        )
        // Description
        Text(
            text = item.description,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

