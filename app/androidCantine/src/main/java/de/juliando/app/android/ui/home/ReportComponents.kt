package de.juliando.app.android.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import de.juliando.app.android.CantineTheme

import de.juliando.app.models.objects.Content


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReportCard(
    item: Content.Report,
    //requestManager: RequestManager
) {
    Column(
        modifier = Modifier.size(305.dp, 200.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Picture
        GlideImage(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            model = item.picture,
            contentDescription = item.title
        ) {
            it
                .error(item.picture)
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

@Preview
@Composable
fun ReportCardPreview() {
    val item = ReportCard(item = )

    ReportCard(
        item = ,
    )
}