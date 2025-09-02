package com.example.finanzas.ui.balance

import com.example.finanzas.data.local.entity.Moneda

data class BalanceState(
    val isLoading: Boolean = false,
    val usedCurrencies: List<Moneda> = emptyList(),
    val selectedCurrency: Moneda? = null,
    val totalIngresos: Double = 0.0,
    val totalGastos: Double = 0.0,
    val balanceNeto: Double = 0.0,
    val tasaAhorro: Float = 0f,
    val monthlyFlows: List<MonthlyFlow> = emptyList()
)

data class MonthlyFlow(
    val month: String,
    val ingresos: Float,
    val gastos: Float
)
