package com.example.finanzas.ui.monthly_goal

import com.example.finanzas.data.local.entity.Moneda

data class MonthlyGoalState(
    val ahorroAcumulado: Double = 0.0, // This might need reconsideration, is it currency-specific?
    val ingresosDelMes: Double = 0.0,
    val gastosDelMes: Double = 0.0,
    val balanceDelMes: Double = 0.0,
    val saldoActual: Double = 0.0,
    val savingsRate: Float = 0f,
    val isLoading: Boolean = true,
    val usedCurrencies: List<Moneda> = emptyList(),
    val selectedCurrency: Moneda? = null,
    val monthlyGoal: Double = 0.0
)