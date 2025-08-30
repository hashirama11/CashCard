package com.example.finanzas.ui.dashboard

import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.TransactionWithDetails

data class DashboardPanelState(
    val currencySymbol: String,
    val totalIncome: Double,
    val totalExpenses: Double,
    val incomeTransactions: List<TransactionWithDetails>,
    val expenseTransactions: List<TransactionWithDetails>,
    val incomeChartData: List<PieChartData>,
    val expenseChartData: List<PieChartData>
)
