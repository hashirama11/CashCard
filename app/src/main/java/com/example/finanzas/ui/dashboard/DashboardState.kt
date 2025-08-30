package com.example.finanzas.ui.dashboard

import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.TransactionWithDetails

data class DashboardState(
    // Data for the selected currency
    val transactions: List<TransactionWithDetails> = emptyList(),
    val totalIngresos: Double = 0.0,
    val totalGastos: Double = 0.0,
    val totalAhorros: Double = 0.0,
    val balanceNeto: Double = 0.0,
    val incomeChartData: List<PieChartData> = emptyList(),
    val expenseChartData: List<PieChartData> = emptyList(),

    // General State
    val isLoading: Boolean = true,
    val userName: String = "",
    val usedCurrenciesInMonth: List<Moneda> = emptyList(),
    val selectedCurrency: Moneda? = null
)