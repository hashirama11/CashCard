package com.example.finanzas.ui.balance

import com.example.finanzas.data.local.entity.Moneda

// Estado que representa los datos para el gráfico de barras mensual
data class MonthlyFlow(
    val month: String,
    val ingresos: Float,
    val gastos: Float
)

// Estado general de la pantalla de balance
data class BalanceState(
    val totalIngresos: Double = 0.0,
    val totalGastos: Double = 0.0,
    val balanceNeto: Double = 0.0,
    val tasaAhorro: Float = 0f,
    val monthlyFlows: List<MonthlyFlow> = emptyList(),
    val isLoading: Boolean = true,
    val usedCurrencies: List<Moneda> = emptyList(),
    val selectedCurrency: Moneda? = null
)