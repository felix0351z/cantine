package de.juliando.app.android.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.R
import de.juliando.app.android.ui.components.Meal
import de.juliando.app.android.ui.components.ShimmerItem
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.utils.DataState
import de.juliando.app.models.objects.ui.Meal
import org.koin.androidx.compose.koinViewModel

const val START_PADDING = 10
const val SPACED_BY = 10

const val MEAL_CARD_HEIGHT_MINIMUM = 130
const val MEAL_CARD_HEIGHT_MAXIMUM = 200

const val REPORT_CARD_WIDTH = 350
const val REPORT_CARD_HEIGHT = 200

const val CORNER_SHAPE = 16

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
                            contentDescription = ""
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
        
    ) { topPadding ->

        val posts by viewModel.posts.collectAsStateWithLifecycle()
        val categories by viewModel.categories.collectAsStateWithLifecycle()
        val selectedMeals by viewModel.selectedMeals.collectAsStateWithLifecycle()


        LazyColumn(
            modifier = Modifier.padding(top = topPadding.calculateTopPadding(), start = START_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(SPACED_BY.dp),
            state = rememberLazyListState(),
        ) {
            item { HomeSection(title = stringResource(R.string.menu_report_title)) } /*News feed section*/

            item { /*News feed item*/
                ReportList(
                    state = rememberLazyListState(),
                    cardSize = DpSize(width = REPORT_CARD_WIDTH.dp, height = REPORT_CARD_HEIGHT.dp),
                    spaceBetween = SPACED_BY.dp*2,
                    items = posts
                )
            }
            
            item { HomeSection(title = stringResource(R.string.menu_meal_title)) } /*Menu section*/

            item {
                MealTabs(onClick = {}, categories = categories)
            }

            when(selectedMeals) {
                is DataState.Success<*> -> {
                    val meals = (selectedMeals as DataState.Success<*>).value as List<Meal>

                    items(
                        count = meals.size,
                        key = { meals[it].id }
                    ) {
                        Meal(
                            modifier = Modifier.clip(RoundedCornerShape(CORNER_SHAPE.dp)),
                            heightIn = Pair(MEAL_CARD_HEIGHT_MINIMUM.dp, MEAL_CARD_HEIGHT_MAXIMUM.dp),
                            item = meals[it]
                        )
                    }
                }
                is DataState.Loading -> {
                    items(count = 5) {
                        ShimmerItem(modifier = Modifier
                            .clip(RoundedCornerShape(CORNER_SHAPE.dp))
                            .fillMaxWidth()
                            .height(MEAL_CARD_HEIGHT_MINIMUM.dp)
                        )
                    }
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
) {
    Spacer(modifier = Modifier.height(padding))
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge
    )
    Spacer(modifier = Modifier.height(padding/4))
}



@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    HomeScreen()
}