package de.juliando.app.android.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.juliando.app.android.ui.theme.CantineColors

@Composable
fun PaymentScreen() {
    Box(modifier = Modifier.background(CantineColors.backgroundColor).fillMaxSize())
}