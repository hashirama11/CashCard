package com.example.finanzas.ui.balance

// Estado que representa los datos para el gráfico de barras mensual
data class MonthlyFlow(
    val month: String,
    val ingresos: Float,
    val gastos: Float
)

// Estado general de la pantalla de balance
data class BalanceState(
    // --- CAMPOS MODIFICADOS PARA MULTIMONEDA ---
    val totalIngresosVes: Double = 0.0,
    val totalIngresosUsd: Double = 0.0,
    val totalGastosVes: Double = 0.0,
    val totalGastosUsd: Double = 0.0,
    val balanceNetoVes: Double = 0.0,
    val balanceNetoUsd: Double = 0.0,
    val tasaAhorro: Float = 0f, // Se mantiene como un % general
    val monthlyFlows: List<MonthlyFlow> = emptyList(),
    val isLoading: Boolean = true
)