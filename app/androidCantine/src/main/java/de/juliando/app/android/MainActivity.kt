package de.juliando.app.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.juliando.app.android.ui.theme.CantineApplicationTheme
import de.juliando.app.android.ui.utils.androidModule
import de.juliando.app.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(listOf(dataModule, androidModule))
        }

        setContent {
            CantineApplicationTheme {
                AppNavigator()
            }
        }
    }
}
