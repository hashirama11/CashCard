package com.example.finanzas.ui.monthly_goal

data class MonthlyGoalState(
    val ahorroAcumulado: Double = 0.0,
    val ingresosDelMesUsd: Double = 0.0,
    val ingresosDelMesVes: Double = 0.0,
    val gastosDelMesUsd: Double = 0.0,
    val gastosDelMesVes: Double = 0.0,
    val balanceDelMesUsd: Double = 0.0,
    val balanceDelMesVes: Double = 0.0,
    val saldoActualUsd: Double = 0.0,
    val saldoActualVes: Double = 0.0,
    val savingsRateUsd: Float = 0f,
    val savingsRateVes: Float = 0f, // <-- NUEVO CAMPO
    val isLoading: Boolean = true
)