package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.TransactionWithDetails

private const val DASHBOARD_TRANSACTION_LIMIT = 5

@Composable
fun RecentTransactions(
    transactions: List<TransactionWithDetails>,
    onTransactionClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transacciones Recientes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            if (transactions.size > DASHBOARD_TRANSACTION_LIMIT) {
                TextButton(onClick = onSeeAllClick) {
                    Text("Ver todas")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Se reemplaza LazyColumn por una Column normal
        Column {
            // Se itera sobre la lista con un forEach en lugar de 'items'
            transactions.take(DASHBOARD_TRANSACTION_LIMIT).forEach { transactionWithDetails ->
                TransactionItem(
                    transactionDetails = transactionWithDetails,
                    onClick = { onTransactionClick(transactionWithDetails.transaccion.id) }
                )
            }
        }
    }
}