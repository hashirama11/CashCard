package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails

@Composable
fun DashboardContent(
    balance: Double,
    transactions: List<TransactionWithDetails>,
    type: TipoTransaccion,
    onTransactionClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BalanceCard(balance = balance, type = type)
        Spacer(modifier = Modifier.height(24.dp))

        if (transactions.isEmpty()) {
            EmptyState(modifier = Modifier.padding(bottom = 60.dp))
        } else {
            RecentTransactions(
                transactions = transactions,
                onTransactionClick = onTransactionClick
            )
        }
    }
}