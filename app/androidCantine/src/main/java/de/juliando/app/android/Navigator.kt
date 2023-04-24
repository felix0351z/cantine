package de.juliando.app.android

import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

import de.juliando.app.android.ui.home.HomeScreen
import de.juliando.app.android.ui.home.views.ReportView
import de.juliando.app.android.ui.orders.OrderScreen
import de.juliando.app.android.ui.payment.PaymentScreen
import de.juliando.app.android.ui.theme.CantineApplicationTheme
import de.juliando.app.android.ui.theme.CantineTheme


sealed class NavigationItem(
    val route: String,
    val displayName: String,
    val selected: ImageVector,
    val unselected: ImageVector
    ) {
    object Home: NavigationItem(
        route = "home",
        displayName = "Menu",
        selected = Icons.Filled.Home,
        unselected = Icons.Outlined.Home
    )
    object Orders: NavigationItem(
        route = "orders",
        displayName = "Bestellungen",
        selected = Icons.Filled.CalendarToday,
        unselected = Icons.Outlined.CalendarToday
    )
    object Payment: NavigationItem(
        route = "payment",
        displayName = "Bezahlung",
        selected = Icons.Filled.Payments,
        unselected = Icons.Outlined.Payments
    )
}

val bottomNavigationBar = listOf(
    NavigationItem.Home,
    NavigationItem.Orders,
    NavigationItem.Payment
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppNavigator() {
    val navController = rememberAnimatedNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(), // Fill the complete screen
        bottomBar = {

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentScreen = navBackStackEntry?.destination

            NavigationBar(
                containerColor = Color.Transparent
            ) {
                bottomNavigationBar.forEach { navigationItem ->
                    val selected = currentScreen?.hierarchy?.any { it.route == navigationItem.route } == true

                    NavigationBarItem(
                        colors = CantineTheme.navigationBarItemColors(),
                        icon = {
                               Icon(
                                   imageVector = if(selected) navigationItem.selected else navigationItem.unselected,
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
            nav_controller = navController,
            padding = innerPadding
        )
    }

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigationHost(
    nav_controller: NavHostController,
    padding: PaddingValues
) {
    AnimatedNavHost(
        navController = nav_controller,
        startDestination = NavigationItem.Home.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(NavigationItem.Home.route) { HomeScreen() }

        composable(
            route = "reportView"
        ) { ReportView() }

        composable(NavigationItem.Orders.route) { OrderScreen() }
        composable(NavigationItem.Payment.route) { PaymentScreen() }


    }

}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    CantineApplicationTheme {
        AppNavigator()
    }
}

