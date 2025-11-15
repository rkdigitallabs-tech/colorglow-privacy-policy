package com.allsoundspro.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.allsoundspro.ui.screens.home.HomeScreen
import com.allsoundspro.ui.screens.player.PlayerScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Home.route
    ) {
        composable(Route.Home.route) {
            HomeScreen(
                onNavigateToPlayer = {
                    navController.navigate(Route.Player.route)
                }
            )
        }

        composable(Route.Player.route) {
            PlayerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
