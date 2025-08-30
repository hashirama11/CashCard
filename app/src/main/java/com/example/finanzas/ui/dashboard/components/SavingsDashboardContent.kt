package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

        item {
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
        }

        if (savingsChartData != null && savingsChartData.points.size > 1) {
            item {
                Text(
                    "Progreso de Ahorro Total (en $selectedSavingsCurrency)",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SavingsChart(savingsChartData = savingsChartData)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            Text(
                "Movimientos de Ahorro del Mes (en $selectedSavingsCurrency)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (filteredTransactions.isEmpty()) {
            item {
                EmptyState(modifier = Modifier.padding(bottom = 60.dp))
            }
        } else {
            items(filteredTransactions) { transactionDetails ->
                TransactionItem(
                    transactionDetails = transactionDetails,
                    onClick = { onTransactionClick(transactionDetails.transaccion.id) }
                )
            }
        }
    }
}
