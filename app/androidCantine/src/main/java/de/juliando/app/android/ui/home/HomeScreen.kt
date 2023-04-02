package de.juliando.app.android.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.ui.components.Meal
import de.juliando.app.android.ui.theme.CantineTheme
import org.koin.androidx.compose.koinViewModel

const val TAG = "HomeScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {

    val state by viewModel.state.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                colors = CantineTheme.largeTopAppBarColors(),
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ },
                        colors = IconButtonDefaults.iconButtonColors(
                        //contentColor = CantineTheme.white
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Einkaufswagen"
                        )
                    }
                },
                title = {},
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
        when(state) {
            is HomeDataState.Success -> {
                Log.d(TAG, "Home screen loaded successfully!")

                HomeFinished(
                    modifier = Modifier.padding(top = it.calculateTopPadding(), start = 10.dp),
                    state = state as HomeDataState.Success
                )
            }
            is HomeDataState.Loading -> {
                Log.d(TAG, "State: Loading....")
                //TODO: Loading animation. Reports and meals could also be packed together as state
            }
            is HomeDataState.Error -> {
                Log.e(TAG, "Error occurred while loading", (state as HomeDataState.Error).exception)
                //TODO: Error screen or snackbar maybe?
            }
        }




    }

}


@Composable
fun HomeFinished(
    modifier: Modifier = Modifier,
    state: HomeDataState.Success
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        state = rememberLazyListState(),
    ) {

        item { // News feed item
            val reportCardSize = DpSize(width = 350.dp, height = 200.dp)
            HomeSection(title = "Neuigkeiten") {
                ReportList(
                    state = rememberLazyListState(),
                    cardSize = reportCardSize,
                    spaceBetween = 20.dp,
                    items = state.reports
                )
            }
        }

        item {// Menu selection bar
            HomeSection(title = "Menü") {
                MealTabs(listOf("Essen", "Getränke", "Wochenplan", "Müll"))
            }
        }

        items(
            count = state.meals.size,
            key = { state.meals[it].id }
        ) {
            Meal(item = state.meals[it])
        }

    }
}

@Composable
fun HomeLoading() {

}

@Composable
fun HomeError() {


}

@Composable
fun HomeSection(
    title: String,
    padding: Dp = 20.dp,
    Item: @Composable () -> Unit
) {
    Spacer(modifier = Modifier.height(padding))
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge
    )
    Spacer(modifier = Modifier.height(padding))
    Item()
}




@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    HomeScreen()
}