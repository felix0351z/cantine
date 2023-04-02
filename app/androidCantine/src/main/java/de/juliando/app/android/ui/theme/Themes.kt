package de.juliando.app.android.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object CantineTypography {


    object Headlines {

        // Used for report headline
        val pictureHeadlineLarge = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        val mealHeadline = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
        )
    }

    object Bodies {

        // Used for report date description in picture
        val pictureBodyLarge = TextStyle(
            color = CantineColors.white50Transparent,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        val mealBody = TextStyle(
            color = CantineTheme.grey2,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,

            )

        // Used for chips (Example: Report)
        val chipBody = TextStyle(
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        val descriptionBody = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )




    }



}

object CantineColors {
    val white = Color.White
    val black = Color.Black
    val primaryColor = Color(0xFFFFBA52)

    val white50Transparent = Color(0x80FFFFFF)
    val black50Transparent = Color(0xCC000000)

    val blackTransparentGradient = Brush.verticalGradient(colors = listOf(Color.Transparent, black50Transparent))


}

