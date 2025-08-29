package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.SavingsChartData
import com.example.finanzas.model.TransactionWithDetails
import com.example.finanzas.ui.theme.AccentGreen

@Composable
fun SavingsDashboardContent(
    totalAhorrosVes: Double,
    totalAhorrosUsd: Double,
    savingsChartData: SavingsChartData?,
    transactions: List<TransactionWithDetails>,
    onTransactionClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            BalanceCard(
                balanceVes = totalAhorrosVes,
                balanceUsd = totalAhorrosUsd,
                label = "Ahorro del Mes",
                color = AccentGreen
            )
        }
        item {
            if (savingsChartData != null && savingsChartData.points.size > 1) {
                SavingsChart(savingsChartData = savingsChartData)
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text("No hay suficientes datos para mostrar el gráfico de progreso de ahorro.")
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Movimientos de Ahorro del Mes", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(transactions) { transactionDetails ->
            TransactionItem(
                transactionDetails = transactionDetails,
                onClick = { onTransactionClick(transactionDetails.transaccion.id) }
            )
        }
    }
}
