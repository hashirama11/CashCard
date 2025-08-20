package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.TransactionWithDetails

@Composable
fun RecentTransactions(
    transactions: List<TransactionWithDetails>,
    onTransactionClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Transacciones Recientes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(transactions) { transactionWithDetails ->
                TransactionItem(
                    transactionDetails = transactionWithDetails,
                    onClick = { onTransactionClick(transactionWithDetails.transaccion.id) }
                )
            }
        }
    }
}