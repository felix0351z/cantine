package de.juliando.app.android.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.LoginActivity
import de.juliando.app.android.R
import de.juliando.app.android.ui.components.Meal
import de.juliando.app.android.ui.components.MultiSelectionTab
import de.juliando.app.android.ui.components.SelectionTab
import de.juliando.app.android.ui.components.ShimmerItem
import de.juliando.app.android.ui.home.views.SelectBottomSheet
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.utils.ViewState

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
fun HomeScreen(
    viewModel: HomeViewModel,
    onReportClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val currentBottomSheetMeal by viewModel.currentBottomSheetMeal.collectAsStateWithLifecycle()


    if (currentBottomSheetMeal != null) {
        SelectBottomSheet(
            onDismiss = viewModel::updateBottomSheetState,
            meal = currentBottomSheetMeal!!
        )
    }

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
                    IconButton(onClick = {
                        //TODO: change Activity and delete cookie
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        viewModel.logout()
                    }) {
                        Icon(imageVector = Icons.Outlined.Logout , contentDescription = "")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.MoreVert , contentDescription = "")
                    }

                }

            )
            
            
        }
        
    ) { topPadding ->

        val viewState by viewModel.state.collectAsStateWithLifecycle()
        val selectedMeals by viewModel.selectedMeals.collectAsStateWithLifecycle()
        val posts by viewModel.posts.collectAsStateWithLifecycle()

        val categories by viewModel.categories.collectAsStateWithLifecycle()
        val tags by viewModel.tags.collectAsStateWithLifecycle()

        val searchInput by viewModel.searchInput.collectAsStateWithLifecycle()
        val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
        val selectedTags by viewModel.selectedTags.collectAsStateWithLifecycle()


        LazyColumn(
            modifier = Modifier.padding(top = topPadding.calculateTopPadding(), start = START_PADDING.dp),
            verticalArrangement = Arrangement.spacedBy(SPACED_BY.dp),
            state = rememberLazyListState(),
        ) {

            item {
                SearchBar( // Search Bar
                    modifier = Modifier
                        .padding(top = SPACED_BY.dp * 2, end = START_PADDING.dp)
                        .fillMaxWidth(),
                    value = searchInput,
                    onValueChange = viewModel::updateSearchText,
                    onSearchPressed = { focusManager.clearFocus() }
                )
            }

            item {
                AnimatedVisibility(visible = searchInput.isEmpty()) {
                    HomeSection(title = stringResource(R.string.menu_report_title))
                }
            }

            item { /*News feed items*/
                AnimatedVisibility(visible = searchInput.isEmpty()) {
                    ReportList(
                        state = rememberLazyListState(),
                        cardSize = DpSize(width = REPORT_CARD_WIDTH.dp, height = REPORT_CARD_HEIGHT.dp),
                        spaceBetween = SPACED_BY.dp*2,
                        reports = posts,
                        status = viewState,
                        onClick = onReportClick
                    )
                }
            }

            item {
                AnimatedVisibility(visible = searchInput.isEmpty()) {
                    HomeSection(title = stringResource(R.string.menu_meal_title))
                }

                AnimatedVisibility(
                    visible = searchInput.isNotEmpty(),
                    enter = slideInVertically()
                ) {
                    MultiSelectionTab(
                        onClick = viewModel::updateTagEntries,
                        tags = tags,
                        selected = selectedTags
                    )
                }
            }

            item {
                SelectionTab(
                    onClick = viewModel::updateCategorySelection,
                    categories = categories,
                    selected = selectedCategory
                )
            }

            when(viewState) {
                is ViewState.Success -> {
                    items(
                        count = selectedMeals.size,
                        key = { selectedMeals[it].id }
                    ) {
                        Meal(
                            modifier = Modifier.clip(RoundedCornerShape(CORNER_SHAPE.dp)),
                            heightIn = Pair(MEAL_CARD_HEIGHT_MINIMUM.dp, MEAL_CARD_HEIGHT_MAXIMUM.dp),
                            item = selectedMeals[it],
                            onClick = { viewModel.updateBottomSheetState(it) }
                        )
                    }
                }
                is ViewState.Loading -> {
                    items(count = 5) {
                        ShimmerItem(modifier = Modifier
                            .clip(RoundedCornerShape(CORNER_SHAPE.dp))
                            .fillMaxWidth()
                            .height(MEAL_CARD_HEIGHT_MINIMUM.dp)
                        )
                    }
                }
                is ViewState.Error -> {
                    TODO()
                }
            }
        }

        BackHandler(enabled = searchInput.isNotEmpty()) {
            viewModel.updateSearchText("")
            focusManager.clearFocus()
        }

    }
}

@Composable
fun HomeSection(
    title: String
) {
    Column {
        Spacer(modifier = Modifier.height(SPACED_BY.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge
        )
    }

}



@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
}