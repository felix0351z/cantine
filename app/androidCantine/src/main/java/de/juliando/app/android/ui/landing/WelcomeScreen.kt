package de.juliando.app.android.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.juliando.app.android.R
import de.juliando.app.android.ui.theme.CantineTheme

@Composable
fun WelcomeScreen(
    onClick: () -> Unit
){

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(
                color = CantineTheme.backgroundColor
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        item {
            Spacer(modifier = Modifier.padding(vertical = 20 .dp))
        }

        item {
            Image(painter = painterResource(
                id = R.drawable.pot_of_food),
                contentDescription = "Pot of food",
                modifier = Modifier
                    .size(size = 300.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.padding(vertical = 10 .dp))
        }

        item {
            val introduction_welcome_to = stringResource(R.string.introduction_welcome_to)
            val introduction_cantine = " ${stringResource(R.string.introduction_mensa)}"

            Box(modifier = Modifier.padding(horizontal = 20.dp).width(370.dp)){
                Text(
                    buildAnnotatedString {
                        withStyle(style = ParagraphStyle(
                            lineHeight = 43.sp
                        )) {
                            withStyle(style = SpanStyle(color = CantineTheme.white, fontSize = 45.sp, fontWeight = FontWeight.Bold)) {
                                append(introduction_welcome_to)
                            }
                            withStyle(style = SpanStyle(color = CantineTheme.primaryColor, fontSize = 45.sp, fontWeight = FontWeight.Bold)) {
                                append(introduction_cantine)
                            }
                        }
                    },
                    modifier = Modifier
                        .width(330.dp)
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterStart)
                )
            }

        }

        item {
            Spacer(modifier = Modifier.padding(vertical = 4 .dp))
        }

        item {
            Text(
                text = stringResource(id = R.string.mensa_description),
                color = CantineTheme.grey1,
                fontSize = 15.sp,
                modifier = Modifier
                    .width(370.dp)
                    .padding(horizontal = 20.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.padding(vertical = 24 .dp))
        }

        item {
            LandingButton(
                modifier = Modifier,
                text = stringResource(id = R.string.continue_btn),
                onClick = onClick
            )
        }

        item {
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}