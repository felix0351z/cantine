package de.juliando.app.android.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import de.juliando.app.android.R
import de.juliando.app.android.ui.theme.CantineTypography

@Composable
fun BoxScope.ErrorMessage() {
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = stringResource(R.string.error_occurred),
        textAlign = TextAlign.Center,
        style = CantineTypography.Bodies.emptyViewStyle
    )
}