package com.example.finanzas.ui.navigation

sealed class AppScreens(val route: String) {
    object Dashboard : AppScreens("dashboard")
    object AddTransaction : AppScreens("add_transaction?transactionId={transactionId}") {
        fun createRoute() = "add_transaction"
        fun createRouteForEdit(transactionId: Int) = "add_transaction?transactionId=$transactionId"
    }
    object Profile : AppScreens("profile")
    object TransactionDetail : AppScreens("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: Int) = "transaction_detail/$transactionId"
    }
}