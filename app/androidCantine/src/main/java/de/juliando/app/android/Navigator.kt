package de.juliando.app.android

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import de.juliando.app.android.ui.home.HomeScreen
import de.juliando.app.android.ui.home.report.ReportScreen
import de.juliando.app.android.ui.orders.OrdersScreen
import de.juliando.app.android.ui.orders.views.OrderScreen
import de.juliando.app.android.ui.payment.PaymentScreen
import de.juliando.app.android.ui.scanner.ScannerScreen
import de.juliando.app.android.ui.scanner.verify_order.VerifyOrderScreen
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.utils.ConnectivityStatus
import de.juliando.app.data.LocalDataStore
import de.juliando.app.models.objects.backend.Auth
import org.koin.androidx.compose.koinViewModel

/**
* Memory enum for all needed navigation routes along the app
 * @param route The route of the screen
 * @param displayName The name of the screen
 * @param image The image if it uses the bottom navigation bar. Instead it can be null
 *
**/
enum class NavigationItem(
    val route: String,
    val displayName: String,
    val navigationArguments: List<NamedNavArgument> = emptyList(),
    val image: Pair<ImageVector, ImageVector>? = null
) {

    HOME(
        route = "home",
        displayName = "Menu",
        image = Pair(Icons.Filled.Home, Icons.Outlined.Home)
    ),
    ORDERS(
        route = "orders",
        displayName = "Bestellungen",
        image = Pair(Icons.Filled.CalendarToday, Icons.Outlined.CalendarToday)
    ),
    PAYMENT (
        route = "payment",
        displayName = "Bezahlung",
        image = Pair(Icons.Filled.Payments, Icons.Outlined.Payments)
    ),
    SCANNER (
        route = "scanner",
        displayName = "Scannen",
        image = Pair(Icons.Filled.QrCodeScanner, Icons.Outlined.QrCodeScanner)
    ),
    REPORT(
        route = "report/{reportId}",
        displayName = "Reports",
        navigationArguments = listOf(
            navArgument("reportId") {
                this.type = NavType.StringType
            }
        )
    ),
    ORDER(
        route = "order/{orderId}",
        displayName = "Order",
        navigationArguments = listOf(
            navArgument("orderId") {
                this.type = NavType.StringType
            }
        )
    ),
    VERIFYORDER(
        route = "verifyOrder/{order}",
        displayName = "VerifyOrder",
        navigationArguments = listOf(
            navArgument("order") {
                this.type = NavType.StringType
            }
        )
    );


    fun hasNoBottomNavigation(): Boolean {
        return this.image == null
    }

    companion object {

        val bottomList: List<NavigationItem>
            get() = values().filter { it.image != null }

        fun fromRoute(route: String): NavigationItem =
            values().find { it.route == route }!!

    }

}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator() {
    val navController = rememberAnimatedNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(), // Fill the complete screen
        topBar = {
                 ConnectivityStatus()
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentScreen = navBackStackEntry?.destination
            val route = currentScreen?.route

            // Check if the current screen is part of the bottom navigation bar.
            // If not no bar is needed
            if (route != null && NavigationItem.fromRoute(route).hasNoBottomNavigation()) {
                return@Scaffold
            }

            NavigationBar(
                containerColor = Color.Transparent
            ) {
                NavigationItem.bottomList.forEach { navigationItem ->

                    val currentUser = LocalDataStore.getCurrentUser()

                    // Only show the scanner to users with the permission level admin or worker
                    if (currentUser != null) {
                        if(navigationItem.route=="scanner" && currentUser.permissionLevel==Auth.PermissionLevel.USER){
                            return@NavigationBar
                        }
                    }

                    // Check if the current interated element is selected
                    val selected = currentScreen?.hierarchy?.any { it.route == navigationItem.route } == true

                    NavigationBarItem(
                        colors = CantineTheme.navigationBarItemColors(),
                        icon = {
                               Icon(
                                   imageVector = if(selected) navigationItem.image!!.first else navigationItem.image!!.second,
                                   contentDescription = navigationItem.displayName,
                               )
                        },
                        label = {
                            Text(
                                text = navigationItem.displayName
                            )
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(navigationItem.route) {

                                // Remove the current screen from the back stack, that the user don't have back problems with the button
                                popUpTo(navController.graph.findStartDestination().id) {
                                    // Store the state if the user navigates back later
                                    saveState = true
                                }
                                // Avoid multiple copies of this screen composable
                                launchSingleTop = true
                                // Restore the last state, if the screen was selected again
                                restoreState = true

                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        AppNavigationHost(
            navController = navController,
            padding = innerPadding
        )
    }

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigationHost(
    navController: NavHostController,
    padding: PaddingValues
) {
    AnimatedNavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = NavigationItem.HOME.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {


        composable(NavigationItem.HOME.route) {
            HomeScreen(
                viewModel = koinViewModel(),
                onReportClick = {
                    navController.navigate("report/$it")
                }
            )
        }
        composable(NavigationItem.ORDERS.route) {
            OrdersScreen(
                viewModel = koinViewModel(),
                onOrderClick = {
                    navController.navigate("order/$it")
                }
            )
        }
        composable(NavigationItem.PAYMENT.route) {
            PaymentScreen()
        }
        composable(NavigationItem.SCANNER.route) {
            ScannerScreen(
                viewModel = koinViewModel(),
                onQrCodeScanned = {
                    navController.navigate("verifyOrder/$it")
                }
            )
        }
        composable(
            route = NavigationItem.REPORT.route,
            arguments = NavigationItem.REPORT.navigationArguments,
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 1000 })
                },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { 1000 })
            }
        ) {
            ReportScreen(
                viewModel = koinViewModel(),
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavigationItem.ORDER.route,
            arguments = NavigationItem.ORDER.navigationArguments,
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 1000 })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { 1000 })
            }
        ) {
            OrderScreen(
                viewModel = koinViewModel(),
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavigationItem.VERIFYORDER.route,
            arguments = NavigationItem.VERIFYORDER.navigationArguments,
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(animationSpec = tween(300), initialOffsetX = { 1000 })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { 1000 })
            }
        ) {
            VerifyOrderScreen(
                viewModel = koinViewModel(),
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }



    }

}