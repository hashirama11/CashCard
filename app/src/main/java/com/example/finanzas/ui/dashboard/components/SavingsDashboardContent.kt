package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.SavingsChartData
import com.example.finanzas.model.TransactionWithDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsDashboardContent(
    modifier: Modifier = Modifier,
    totalAhorrosVes: Double,
    totalAhorrosUsd: Double,
    primaryCurrencySymbol: String,
    secondaryCurrencySymbol: String,
    selectedSavingsCurrency: String?,
    onCurrencySelected: (String) -> Unit,
    savingsChartData: SavingsChartData?,
    transactions: List<TransactionWithDetails>,
    onTransactionClick: (Int) -> Unit
) {
    val filteredTransactions = transactions.filter { it.transaccion.moneda == selectedSavingsCurrency }

    Column(modifier = modifier) {
        SavingsBalanceCard(
            balanceVes = totalAhorrosVes,
            balanceUsd = totalAhorrosUsd,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedSavingsCurrency == primaryCurrencySymbol,
                onClick = { onCurrencySelected(primaryCurrencySymbol) },
                label = { Text("Ahorro en $primaryCurrencySymbol") }
            )
            if (secondaryCurrencySymbol.isNotBlank()) {
                FilterChip(
                    selected = selectedSavingsCurrency == secondaryCurrencySymbol,
                    onClick = { onCurrencySelected(secondaryCurrencySymbol) },
                    label = { Text("Ahorro en $secondaryCurrencySymbol") }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (savingsChartData != null && savingsChartData.points.size > 1) {
            Text(
                "Progreso de Ahorro Total (en $selectedSavingsCurrency)",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SavingsChart(savingsChartData = savingsChartData)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            "Movimientos de Ahorro del Mes (en $selectedSavingsCurrency)",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (filteredTransactions.isEmpty()) {
            EmptyState(modifier = Modifier.padding(bottom = 16.dp))
        } else {
            // Display up to 5 transactions directly in the column
            filteredTransactions.take(5).forEach { transactionDetails ->
                TransactionItem(
                    transactionDetails = transactionDetails,
                    onClick = { onTransactionClick(transactionDetails.transaccion.id) }
                )
            }
        }
    }
}
