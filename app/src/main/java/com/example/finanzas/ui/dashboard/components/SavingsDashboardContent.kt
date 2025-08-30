package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.SavingsChartData
import com.example.finanzas.model.TransactionWithDetails

@Composable
fun SavingsDashboardContent(
    totalAhorrosVes: Double,
    totalAhorrosUsd: Double,
    savingsChartData: SavingsChartData?,
    transactions: List<TransactionWithDetails>,
    onTransactionClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            SavingsBalanceCard(
                balanceVes = totalAhorrosVes,
                balanceUsd = totalAhorrosUsd,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (savingsChartData != null && savingsChartData.points.size > 1) {
            item {
                Text(
                    "Progreso de Ahorro Total",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SavingsChart(savingsChartData = savingsChartData)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            Text(
                "Movimientos de Ahorro del Mes",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (transactions.isEmpty()) {
            item {
                EmptyState(modifier = Modifier.padding(bottom = 60.dp))
            }
        } else {
            items(transactions) { transactionDetails ->
                TransactionItem(
                    transactionDetails = transactionDetails,
                    onClick = { onTransactionClick(transactionDetails.transaccion.id) }
                )
            }
        }
    }
}
