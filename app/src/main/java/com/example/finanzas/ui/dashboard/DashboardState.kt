package com.example.finanzas.ui.dashboard

import com.example.finanzas.model.TransactionWithDetails

data class DashboardState(
    val transactionsWithDetails: List<TransactionWithDetails> = emptyList(),
    val totalIngresos: Double = 0.0,
    val totalGastos: Double = 0.0,
    val isLoading: Boolean = true,
    val userName: String = ""
)