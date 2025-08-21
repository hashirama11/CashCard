package com.example.finanzas.ui.navigation

sealed class AppScreens(val route: String) {
    object Onboarding : AppScreens("onboarding")
    object Dashboard : AppScreens("dashboard")
    object Balance : AppScreens("balance") // <-- NUEVA RUTA
    object AddTransaction : AppScreens("add_transaction?transactionId={transactionId}") {
        fun createRoute() = "add_transaction"
        fun createRouteForEdit(transactionId: Int) = "add_transaction?transactionId=$transactionId"
    }
    object Profile : AppScreens("profile")
    object TransactionDetail : AppScreens("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: Int) = "transaction_detail/$transactionId"
    }
    object AllTransactions : AppScreens("all_transactions")
    object CategoryManagement : AppScreens("category_management")
}