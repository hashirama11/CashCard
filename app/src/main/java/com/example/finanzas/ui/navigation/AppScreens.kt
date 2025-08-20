package com.example.finanzas.ui.navigation

sealed class AppScreens(val route: String) {
    object Dashboard : AppScreens("dashboard")
    object AddTransaction : AppScreens("add_transaction")
    object Profile : AppScreens("profile")
}