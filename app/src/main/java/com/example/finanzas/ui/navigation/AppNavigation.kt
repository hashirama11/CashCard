package com.example.finanzas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.finanzas.ui.add_transaction.AddTransactionScreen
import com.example.finanzas.ui.all_transactions.AllTransactionsScreen
import com.example.finanzas.ui.category_management.CategoryManagementScreen
import com.example.finanzas.ui.dashboard.DashboardScreen
import com.example.finanzas.ui.profile.ProfileScreen
import com.example.finanzas.ui.transaction_detail.TransactionDetailScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppScreens.Dashboard.route) {
        composable(AppScreens.Dashboard.route) {
            DashboardScreen(
                onAddTransaction = {
                    navController.navigate(AppScreens.AddTransaction.createRoute())
                },
                onTransactionClick = { transactionId ->
                    navController.navigate(AppScreens.TransactionDetail.createRoute(transactionId))
                },
                onSeeAllClick = {
                    navController.navigate(AppScreens.AllTransactions.route)
                }
            )
        }
        composable(
            route = AppScreens.AddTransaction.route,
            arguments = listOf(navArgument("transactionId") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            AddTransactionScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(AppScreens.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onCategoryManagementClick = {
                    navController.navigate(AppScreens.CategoryManagement.route)
                }
            )
        }
        composable(
            route = AppScreens.TransactionDetail.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
        ) {
            TransactionDetailScreen(
                onBack = { navController.popBackStack() },
                onEditClick = { transactionId ->
                    navController.navigate(AppScreens.AddTransaction.createRouteForEdit(transactionId))
                }
            )
        }
        composable(AppScreens.AllTransactions.route) {
            AllTransactionsScreen(
                onBack = { navController.popBackStack() },
                onTransactionClick = { transactionId ->
                    navController.navigate(AppScreens.TransactionDetail.createRoute(transactionId))
                }
            )
        }
        composable(AppScreens.CategoryManagement.route) {
            CategoryManagementScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}