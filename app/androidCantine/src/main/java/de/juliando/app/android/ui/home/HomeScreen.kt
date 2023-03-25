package de.juliando.app.android.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.utils.DataState
import de.juliando.app.models.objects.Content
import org.koin.androidx.compose.koinViewModel

const val TAG = "HomeScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {

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
                Log.d(TAG, "Success: Reports to show!")

                val cardSize = DpSize(width = 350.dp, height = 200.dp)
                val startPadding = 10.dp

                ReportList(
                    modifier = Modifier.padding(top = it.calculateTopPadding(), start = startPadding),
                    state = reportsListState,
                    cardSize = cardSize,
                    spaceBetween = startPadding*2,
                    items = (reports as DataState.Success<*>).value as List<Content.Report>
                )

            }
            is DataState.Loading -> {
                Log.d(TAG, "State: Loading....")
                //TODO: Loading animation. Reports and meals could also be packed together as state
            }
            is DataState.Error -> {
                Log.e(TAG, "Error occurred while loading the reports", (reports as DataState.Error).exception)
                //TODO: Error screen or snackbar maybe?
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    HomeScreen()
}