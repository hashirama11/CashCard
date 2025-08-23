package com.example.finanzas.ui.monthly_goal

data class MonthlyGoalState(
    val ahorroAcumulado: Double = 0.0,
    val ingresosDelMes: Double = 0.0,
    val gastosDelMes: Double = 0.0,
    val balanceDelMes: Double = 0.0,
    val saldoActual: Double = 0.0,
    val isLoading: Boolean = true
)