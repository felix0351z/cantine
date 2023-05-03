package de.juliando.app.android.ui.landing

import android.service.autofill.OnClickAction
import android.view.View.OnClickListener
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job

@Composable
fun LandingButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(width = 300.dp, height = 55.dp),
        shape = CutCornerShape(10)
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}