package de.juliando.app.android.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import de.juliando.app.android.R

object CantineTypography {


    object Headlines {

        // Used for report headline
        val headlineLarge = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        val headlineMedium = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
        )

        // Used for OrderedMeal
        val headlineSmall = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
    }

    object Bodies {

        private val charterFont = FontFamily(
            Font(R.font.charter_regular, FontWeight.Normal),
            Font(R.font.charter_bold, FontWeight.Bold),
            Font(R.font.charter_italic, FontWeight.Normal, FontStyle.Italic),
            Font(R.font.charter_bold_italic, FontWeight.Bold, FontStyle.Italic)
        )

        private val libreBaskervilleFont = FontFamily(
            Font(R.font.librebaskerville_regular, FontWeight.Normal),
            Font(R.font.librebaskerville_bold, FontWeight.Bold),
            Font(R.font.librebaskerville_italic, FontWeight.Normal, FontStyle.Italic)
        )


        // Used for report date description in picture
        val pictureBodyLarge = TextStyle(
            color = CantineColors.white50Transparent,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        val title_description_style = TextStyle(
            color = Color(0x99FFFFFF),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )


        val reading_style = TextStyle(
            color = Color(0x99FFFFFF),
            fontWeight = FontWeight.Normal,
            lineHeight = 1.5.em,
            fontSize = 20.sp,
            fontFamily = libreBaskervilleFont
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

        val bodySmall = TextStyle(
            color = CantineTheme.grey2,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )

        val bodySmallSelected = TextStyle(
            color = CantineColors.white,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )

        val primaryButton = TextStyle(
            color = CantineColors.black,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        val emptyViewStyle = TextStyle(
            color = CantineTheme.grey2,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )







    }



}

object CantineColors {
    val white = Color(0xFFD5D5D5)
    val black = Color.Black

    val surfaceColor = Color(0xFF1C1C1C)
    val onSurfaceColor = Color(0xFF272727)
    val backgroundColor = Color(0xFF0E0E0E)

    val primaryColor = Color(0xFFFFBA52)

    val white50Transparent = Color(0x80FFFFFF)
    val black50Transparent = Color(0xCC000000)

    val blackTransparentGradient = Brush.verticalGradient(colors = listOf(Color.Transparent, black50Transparent))


}

