package de.juliando.app.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.juliando.app.android.ui.theme.CantineApplicationTheme
import de.juliando.app.android.utils.androidModule
import de.juliando.app.data.LocalDataStore
import de.juliando.app.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (GlobalContext.getKoinApplicationOrNull() == null) {
            startKoin {
                androidLogger()
                androidContext(this@MainActivity)
                modules(listOf(dataModule, androidModule))
            }
        }

        setContent {
            CantineApplicationTheme {
                if(LocalDataStore.getAuthenticationCookieHeader() == null){
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                }else{
                    AppNavigator()
                }
            }
        }
    }
}
