package de.juliando.app.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.juliando.app.android.ui.theme.CantineTypography

@Composable
fun SimpleChip(
    modifier: Modifier = Modifier,
    text: String,
    chipPadding: Dp = 3.dp,
    color: Color = Color.White

) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
    ) {

        Box(
            modifier = Modifier
                .padding(horizontal = chipPadding*4, vertical = chipPadding)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = text,
                style = CantineTypography.Bodies.chipBody,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }



}