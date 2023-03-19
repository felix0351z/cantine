package de.juliando.app.android.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import de.juliando.app.android.CantineTheme
import de.juliando.app.android.ui.utils.DataState
import de.juliando.app.models.objects.Content

import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {

    val d = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val reports by viewModel.reports.collectAsStateWithLifecycle()
    val reportsListState = rememberLazyListState()


    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = CantineTheme.largeTopAppBarColors(),
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ },
                        colors = IconButtonDefaults.iconButtonColors(
                        //contentColor = CantineTheme.white
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "Einkaufswagen"
                        )
                    }
                },
                title = {

                        Text(
                            text = "Mensa",
                            //modifier = Modifier.fillMaxWidth()
                            style = MaterialTheme.typography.headlineLarge
                        )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.Logout , contentDescription = "")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.MoreVert , contentDescription = "")
                    }

                }

            )
            
            
        }
        
    ) {
        when(reports) {
            is DataState.Success<*> -> {
                val items: List<Content.Report> = (reports as DataState.Success<*>).value as List<Content.Report>

                Log.d("fdbgfd", "Success")

                /*ReportList(
                    items = (reports as DataState.Success<*>).value as List<Content.Report>,
                    state = reportsListState
                )*/
            }
            else -> {
                System.out.println("No Success")
                //TODO
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    HomeScreen()
}