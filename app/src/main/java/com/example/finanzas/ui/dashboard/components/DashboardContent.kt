package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.*
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
        BalanceCard(balance = balance, type = type)
        Spacer(modifier = Modifier.height(24.dp))

        if (transactions.isEmpty()) {
            EmptyState(modifier = Modifier.padding(bottom = 60.dp)) // Padding para que no se pegue al fondo
        } else {
            RecentTransactions(transactions = transactions)
        }
    }
}