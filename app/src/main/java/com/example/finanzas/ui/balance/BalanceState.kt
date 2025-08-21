package com.example.finanzas.ui.balance

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
    val tasaAhorro: Float = 0f, // Un valor entre 0.0 y 1.0
    val monthlyFlows: List<MonthlyFlow> = emptyList(),
    val isLoading: Boolean = true
)