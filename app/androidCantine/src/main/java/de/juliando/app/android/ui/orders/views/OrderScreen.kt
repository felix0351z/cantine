package de.juliando.app.android.ui.orders.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import de.juliando.app.android.R
import de.juliando.app.android.ui.components.ShimmerItem
import de.juliando.app.android.ui.home.report.*
import de.juliando.app.android.ui.orders.toFormattedOrderTime
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.utils.DataState
import de.juliando.app.models.objects.ui.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val PADDING = 20.dp
val LOADING_TITLE_WIDTH = 300.dp
val LOADING_TITLE_HEIGHT = 50.dp
val LOADING_TITLE_CLIP = RoundedCornerShape(10.dp)

val LOADING_BODY_HEIGHT = 20.dp
val LOADING_BODY_CLIP = RoundedCornerShape(4.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    viewModel: OrderViewModel,
    onBackPressed: () -> Unit
) {
    val order by viewModel.order.collectAsStateWithLifecycle()

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
                // When the data is loading
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
                        * Loading animation for the qr-code
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

                // When the data is successfully loaded
                is DataState.Success<*> -> {
                    val value: Order = (order as DataState.Success<*>).value as Order

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(80.dp))

                            Text( // Title
                                modifier = Modifier,
                                text = toFormattedOrderTime(value.orderTime),
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(100.dp))

                            Box(
                                modifier = Modifier
                                    // Border around the qrcode that the scanner recognises the qrcode
                                    .border(8.dp, Color.White, RoundedCornerShape(5))
                                    .size(248.dp)
                            ){
                                // Create image of the qr-Code with the username and the orderID as text
                                Image(
                                    painter = qrBitmapPainter(viewModel.getOrderId()),
                                    contentDescription = "OrderId",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(240.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(140.dp))

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
                    }
                }
                is DataState.Error -> {
                    //TODO()
                }
            }



        }
    }
}

/**
 * Paints the Qr-Code of the id
 */
@Composable
fun qrBitmapPainter(
    content: String,
    size: Dp = 150.dp,
    padding: Dp = 0.dp
): BitmapPainter {

    // get the current density of the screen
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }
    val paddingPx = with(density) { padding.roundToPx() }

    var bitmap by remember(content) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(bitmap) {
        if (bitmap != null) return@LaunchedEffect

        launch(Dispatchers.IO) {
            val qrCodeWriter = QRCodeWriter()

            val encodeHints = mutableMapOf<EncodeHintType, Any?>()
                .apply {
                    this[EncodeHintType.MARGIN] = paddingPx
                }

            val bitmapMatrix = try {
                qrCodeWriter.encode(
                    content, BarcodeFormat.QR_CODE,
                    sizePx, sizePx, encodeHints
                )
            } catch (ex: WriterException) {
                null
            }

            val matrixWidth = bitmapMatrix?.width ?: sizePx
            val matrixHeight = bitmapMatrix?.height ?: sizePx

            val newBitmap = Bitmap.createBitmap(
                bitmapMatrix?.width ?: sizePx,
                bitmapMatrix?.height ?: sizePx,
                Bitmap.Config.ARGB_8888,
            )

            for (x in 0 until matrixWidth) {
                for (y in 0 until matrixHeight) {
                    val shouldColorPixel = bitmapMatrix?.get(x, y) ?: false
                    // Colors of the qrcode
                    val pixelColor = if (shouldColorPixel) CantineColors.backgroundColor else Color.White

                    newBitmap.setPixel(x, y, pixelColor.toArgb())
                }
            }
            bitmap = newBitmap
        }
    }

    return remember(bitmap) {
        val currentBitmap = bitmap ?: Bitmap.createBitmap(
            sizePx, sizePx,
            Bitmap.Config.ARGB_8888,
        ).apply { eraseColor(Color.Transparent.toArgb()) }

        BitmapPainter(currentBitmap.asImageBitmap())
    }
}