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
import de.juliando.app.android.ui.home.menu.SelectBottomSheet
import de.juliando.app.android.ui.home.shopping_cart.ShoppingCartScreen
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.utils.ViewState
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
fun HomeScreen(
    viewModel: HomeViewModel,
    onReportClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val currentBottomSheetMeal by viewModel.currentBottomSheetMeal.collectAsStateWithLifecycle()
    val isShoppingCartSelected by viewModel.isShoppingCartSelected.collectAsStateWithLifecycle()
    val snackbarItem by viewModel.snackbar.collectAsStateWithLifecycle()

    /*
    * Show the bottom sheet to select a meal.
    * If the user clicks on a meal, the currentBottomSheetMeal flow will be updated to the current meal
    * and the bottom sheet will be displayed.
    * If the user exits the sheet, the current meal will be set to null again
    */
    if (currentBottomSheetMeal != null) {
        SelectBottomSheet(
            onDismiss = viewModel::onMealClick,
            meal = currentBottomSheetMeal!!,
            onFinish = viewModel::onShoppingCartAdd
        )
    }

    /*
    * Show the shopping cart sheet if the user has clicked
    * on the icon.
    * The action will be handled also with a stateflow
    */
    if (isShoppingCartSelected) {
        ShoppingCartScreen(
            onDismiss = viewModel::onShoppingCartClick,
            viewModel = koinViewModel(),
            onOrderCreated = viewModel::onPayment
        )
    }

    /*
    * Show all snack-bars which will be sent by the view model.
    */
    LaunchedEffect(snackbarItem) {
        if (snackbarItem != null) { // Only if the item is not null
            val result = snackbarHostState.showSnackbar(
                message = context.resources.getString(snackbarItem!!.message),
                actionLabel = if (snackbarItem!!.button != null) context.resources.getString(snackbarItem!!.button!!.name) else null,
                duration = SnackbarDuration.Short
            )
            when(result) {
                SnackbarResult.ActionPerformed -> {
                    snackbarItem!!.button!!.action()
                    viewModel.resetSnackbar()
                }
                SnackbarResult.Dismissed -> {
                    viewModel.resetSnackbar()
                }
            }
        }
    }


    /*
    * Main screen
    */
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = CantineTheme.largeTopAppBarColors(),
                navigationIcon = {
                    // Shopping cart icon for the sheet
                    IconButton(onClick = viewModel::onShoppingCartClick) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = stringResource(R.string.shopping_cart_title)
                        )
                    }
                },
                // No title needed, because the home screen will be separated into sections with own titles
                title = {},
                actions = {
                    // Log out icon
                    IconButton(onClick = {
                        viewModel.logout(onLogoutSuccessful = {context.startActivity(Intent(context, LoginActivity::class.java))})
                    }) {
                        Icon(imageVector = Icons.Outlined.Logout , contentDescription = stringResource(R.string.logout_title))
                    }
                    // More action
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Outlined.MoreVert , contentDescription = stringResource(R.string.more_title))
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
                            heightIn = Pair(MEAL_CARD_HEIGHT_MINIMUM.dp, MEAL_CARD_HEIGHT_MAXIMUM.dp),
                            item = selectedMeals[it],
                            onClick = { viewModel.onMealClick(it) }
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