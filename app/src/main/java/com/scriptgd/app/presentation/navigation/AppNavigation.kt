package com.scriptgd.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scriptgd.app.presentation.auth.LoginScreen
import com.scriptgd.app.presentation.dashboard.DashboardHubScreen
import com.scriptgd.app.presentation.dashboard.lukisanku.LukisankuScreen

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(onLoggedIn = {
                nav.navigate(Screen.DashboardHub.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }
        composable(Screen.DashboardHub.route) {
            DashboardHubScreen(onOpenLukisanku = { nav.navigate(Screen.Lukisanku.route) })
        }
        composable(Screen.Lukisanku.route) {
            LukisankuScreen(onBack = { nav.popBackStack() })
        }
    }
}
