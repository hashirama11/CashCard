package com.example.finanzas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finanzas.ui.add_transaction.AddTransactionScreen
import com.example.finanzas.ui.dashboard.DashboardScreen
import com.example.finanzas.ui.profile.ProfileScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppScreens.Dashboard.route) {
        composable(AppScreens.Dashboard.route) {
            DashboardScreen(
                onAddTransaction = {
                    navController.navigate(AppScreens.AddTransaction.route)
                },
                onTransactionClick = { transactionId ->
                    // Por ahora lo dejamos vacío, pero ya no dará error
                }
            )
        }
        composable(AppScreens.AddTransaction.route) {
            AddTransactionScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(AppScreens.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}