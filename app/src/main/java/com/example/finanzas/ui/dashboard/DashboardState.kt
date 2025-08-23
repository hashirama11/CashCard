package com.example.finanzas.ui.dashboard

import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.TransactionWithDetails

data class DashboardState(
    val transactionsWithDetails: List<TransactionWithDetails> = emptyList(),
    val totalIngresosVes: Double = 0.0,
    val totalIngresosUsd: Double = 0.0,
    val totalGastosVes: Double = 0.0,
    val totalGastosUsd: Double = 0.0,
    val ahorroAcumulado: Double = 0.0, // <-- NUEVO
    val isLoading: Boolean = true,
    val userName: String = "",
    val expenseChartData: List<PieChartData> = emptyList(),
    val incomeChartData: List<PieChartData> = emptyList()
)