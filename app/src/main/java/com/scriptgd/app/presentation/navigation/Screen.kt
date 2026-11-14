package com.scriptgd.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object DashboardHub : Screen("dashboard_hub")
    data object Lukisanku : Screen("lukisanku")
}
