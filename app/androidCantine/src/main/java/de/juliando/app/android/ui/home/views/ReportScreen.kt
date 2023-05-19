package de.juliando.app.android.ui.home.views

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import de.juliando.app.android.ui.components.ShimmerItem
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.android.utils.DataState
import de.juliando.app.models.objects.ui.Report

val SPACED_BY = 30.dp
val PADDING = 20.dp
val LOADING_TITLE_WIDTH = 300.dp
val LOADING_TITLE_HEIGHT = 50.dp
val LOADING_TITLE_CLIP = RoundedCornerShape(10.dp)

val LOADING_BODY_HEIGHT = 20.dp
val LOADING_BODY_CLIP = RoundedCornerShape(4.dp)

val PICTURE_CORNER_CLIP = RoundedCornerShape(10.dp)
val PICTURE_HEIGHT = 200.dp


private val requestBuilder = { requestBuilder: RequestBuilder<Drawable> ->
    requestBuilder
        .thumbnail()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ReportScreen(
    viewModel: ReportViewModel,
    onBackPressed: () -> Unit
) {

    val report by viewModel.report.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "", tint = Color.White)
                    }
                }

            )

        }
    ) {
        
        LazyColumn(
            modifier = Modifier.padding(
                top = it.calculateTopPadding() + PADDING,
                start = PADDING,
                end = PADDING
            ),
            verticalArrangement = Arrangement.spacedBy(SPACED_BY)
        ) {

            when(report) {
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
                        * Loading animation for the content
                        * Draw multiple description lines
                        */
                        Column(
                            verticalArrangement = Arrangement.spacedBy(PADDING/2)
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
                    val value: Report = (report as DataState.Success<*>).value as Report

                    item {
                        /*
                        * Title Header with the title itself and a short description about the current date and tags
                        */
                        Column {
                            Text( // Title
                                text = value.title,
                                style = MaterialTheme.typography.headlineLarge
                            )

                            ProvideTextStyle(value = CantineTypography.Bodies.title_description_style) {
                                Row {
                                    if (value.creationTime != null) {
                                        Text("${value.creationTime} | ")
                                    }

                                    Text(value.tags.joinToString { it })
                                }
                            }
                        }
                    }

                    item {
                        /*
                        * Show the image to the report if one exists.
                        */
                        if (value.picture != null) {

                            Box(Modifier.fillMaxWidth()) {

                                Box(modifier = Modifier
                                    .clip(PICTURE_CORNER_CLIP)
                                    .align(Alignment.Center)) {

                                    GlideImage(
                                        modifier = Modifier
                                            .height(PICTURE_HEIGHT),
                                        model = value.picture,
                                        contentDescription = value.title,
                                        requestBuilderTransform = requestBuilder
                                    )
                                }

                            }


                        }
                    }


                    item {
                        /*
                        * Print the description.
                        */
                        Text(
                            text = value.description,
                            style = CantineTypography.Bodies.reading_style,
                        )
                    }



                }
                is DataState.Error -> {
                    TODO()
                }


            }






        }
        
        

    }
}