package com.example.finanzas.ui.dashboard

import com.example.finanzas.model.MonthlySummary
import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.SavingsChartData
import com.example.finanzas.model.TransactionWithDetails

data class DashboardState(
    // State for Ingreso/Gasto tabs
    val incomeTransactions: List<TransactionWithDetails> = emptyList(),
    val expenseTransactions: List<TransactionWithDetails> = emptyList(),
    val totalIngresosPrimario: Double = 0.0,
    val totalIngresosSecundario: Double = 0.0,
    val totalGastosPrimario: Double = 0.0,
    val totalGastosSecundario: Double = 0.0,
    val incomeChartData: List<PieChartData> = emptyList(),
    val expenseChartData: List<PieChartData> = emptyList(),

    // State for Ahorro tab
    val savingsTransactions: List<TransactionWithDetails> = emptyList(),
    val totalAhorrosPrimario: Double = 0.0,
    val totalAhorrosSecundario: Double = 0.0,
    val savingsChartData: SavingsChartData? = null,
    val selectedSavingsCurrency: String? = null,

    // State for Resumen tab
    val monthlySummary: List<MonthlySummary> = emptyList(),

    // General State
    val ahorroAcumulado: Double = 0.0,
    val isLoading: Boolean = true,
    val userName: String = "",
    val primaryCurrencySymbol: String = "",
    val secondaryCurrencySymbol: String = "",
    val usedCurrenciesInMonth: List<String> = emptyList(),
    val selectedDashboardCurrency: String? = null
)