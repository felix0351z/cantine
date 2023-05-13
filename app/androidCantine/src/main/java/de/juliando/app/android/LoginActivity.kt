package de.juliando.app.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import de.juliando.app.android.ui.landing.LoginScreen
import de.juliando.app.android.ui.landing.WelcomeScreen
import de.juliando.app.android.ui.theme.CantineApplicationTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CantineApplicationTheme {
                WelcomeView()
            }
        }
    }
}

@Composable
fun WelcomeView(){
    var showWelcome = remember { mutableStateOf(true) }
    BackHandler(enabled = true, onBack = {
        if (!showWelcome.value){
            showWelcome.value = true
        }
    })
    if (showWelcome.value){
        WelcomeScreen(onClick = { showWelcome.value = false })
    }else{
        LoginScreen()
    }
}