package com.example.finanzas.ui.dashboard

import com.example.finanzas.data.local.entity.Transaccion

data class DashboardState(
    val transacciones: List<Transaccion> = emptyList(),
    val totalIngresos: Double = 0.0,
    val totalGastos: Double = 0.0,
    val isLoading: Boolean = true
)