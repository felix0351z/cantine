package de.juliando.app.android.ui.home

import android.annotation.SuppressLint
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
import de.juliando.app.models.objects.ui.Meal
import org.koin.androidx.compose.koinViewModel

const val TAG = "HomeScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
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
        val selectedMeals by viewModel.selectedMeals.collectAsStateWithLifecycle()

        LazyColumn(
            modifier = Modifier.padding(top = it.calculateTopPadding(), start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            state = rememberLazyListState(),
        ) {

            item { // News feed item
                val reportCardSize = DpSize(width = 350.dp, height = 200.dp)
                HomeSection(title = "Neuigkeiten") {
                    val posts by viewModel.posts.collectAsStateWithLifecycle()

                    ReportList(
                        state = rememberLazyListState(),
                        cardSize = reportCardSize,
                        spaceBetween = 20.dp,
                        items = posts
                    )
                }
            }

            item {// Menu selection bar
                HomeSection(title = "Menü") {
                    val categories by viewModel.categories.collectAsStateWithLifecycle()
                    MealTabs(
                        onClick = {},
                        categories = categories
                    )
                }
            }

            when(selectedMeals) {
                is DataState.Success<*> -> {
                    val ite = (selectedMeals as DataState.Success<*>).value as List<Meal>

                    items(
                        count = ite.size,
                        key = { ite[it].id }
                    ) {
                        Meal(item = ite[it])
                    }

                }
                is DataState.Loading -> {


                }
                is DataState.Error -> {


                }
            }
        }
    }
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