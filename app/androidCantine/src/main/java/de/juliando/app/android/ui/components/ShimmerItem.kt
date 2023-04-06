package de.juliando.app.android.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import de.juliando.app.android.ui.theme.CantineColors

@Composable
fun ShimmerItem(modifier: Modifier) = Spacer(modifier = modifier.shimmerEffect())


fun Modifier.shimmerEffect(
    colors: List<Color> = listOf(CantineColors.surfaceColor, CantineColors.onSurfaceColor, CantineColors.surfaceColor)
): Modifier = composed {
    var coordinate by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition()

    val translateAim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(translateAim, 0f),
            end = Offset(translateAim + coordinate.width.toFloat(), coordinate.height.toFloat())
        )
    ).onGloballyPositioned {
        coordinate = it.size
    }
}