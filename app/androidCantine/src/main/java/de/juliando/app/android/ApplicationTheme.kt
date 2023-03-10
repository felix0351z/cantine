package de.juliando.app.android

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object CantineTheme {
    // Standard colors from the figma theme
    val backgroundColor = Color(0xFF0E0E0E)
    val surfaceColor = Color(0xFF1C1C1C)

    val white = Color(0xFFFFFFFF)
    val grey1 = Color(0xFF9C9C9C)
    val grey2 = Color(0xFF585858)

    val primaryColor = Color(0xFFFFBA52)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable fun largeTopAppBarColors() = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = backgroundColor,
        titleContentColor = white,
        actionIconContentColor = white,
        navigationIconContentColor = white
    )
    @Composable fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
        selectedIconColor = primaryColor,
        selectedTextColor = primaryColor,
        indicatorColor = surfaceColor,
        unselectedIconColor = grey1,
        unselectedTextColor = grey1
    )

}

@Composable
fun CantineApplicationTheme(
    content: @Composable () -> Unit
) {


    val colors = darkColorScheme(
        primary = CantineTheme.primaryColor,
        background = CantineTheme.backgroundColor,
        surface = CantineTheme.surfaceColor,
        onSurface = CantineTheme.grey1
    )

    val typography = Typography(

        headlineLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        ),

        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal, // Maybe italic
            fontSize = 12.sp
        )

    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
