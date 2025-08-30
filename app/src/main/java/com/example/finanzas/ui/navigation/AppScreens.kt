package com.example.finanzas.ui.navigation

sealed class AppScreens(val route: String) {
    object Onboarding : AppScreens("onboarding")
    object Dashboard : AppScreens("dashboard")
    // RENOMBRADO: Esta es ahora la pantalla de balance histórico general
    object HistoricalBalance : AppScreens("historical_balance")
    // NUEVO: Una pantalla para el objetivo del mes
    object MonthlyGoal : AppScreens("monthly_goal")
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
    object PurchaseHistory : AppScreens("purchase_history")
    object NotificationSettings : AppScreens("notification_settings")
    object CurrencySettings : AppScreens("currency_settings")
    object AddCustomCurrency : AppScreens("add_custom_currency")
}