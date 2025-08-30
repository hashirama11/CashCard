package com.example.finanzas.ui.dashboard

import com.example.finanzas.model.MonthlySummary
import com.example.finanzas.model.SavingsChartData

data class DashboardState(
    val dashboardPanels: List<DashboardPanelState> = emptyList(),
    val totalAhorrosVes: Double = 0.0,
    val totalAhorrosUsd: Double = 0.0,
    val ahorroAcumulado: Double = 0.0,
    val isLoading: Boolean = true,
    val userName: String = "",
    val primaryCurrencySymbol: String = "",
    val secondaryCurrencySymbol: String = "",
    val monthlySummary: List<MonthlySummary> = emptyList(),
    val savingsChartData: SavingsChartData? = null,
    val selectedSavingsCurrency: String? = null
)