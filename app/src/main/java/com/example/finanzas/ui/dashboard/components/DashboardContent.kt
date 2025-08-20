package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.model.TipoTransaccion

@Composable
fun DashboardContent(
    balance: Double,
    transactions: List<Transaccion>,
    type: TipoTransaccion
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Aquí irán las tarjetas y gráficos
        BalanceCard(balance = balance, type = type)
        Spacer(modifier = Modifier.height(16.dp))
        // TODO: Añadir gráfico de categorías (PieChart)
        // TODO: Añadir lista de transacciones recientes
    }
}