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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    balanceVes: Double,
    balanceUsd: Double,
    primaryCurrencySymbol: String,
    secondaryCurrencySymbol: String,
    usedCurrenciesInMonth: List<String>,
    selectedCurrency: String?,
    onCurrencySelected: (String) -> Unit,
    transactions: List<TransactionWithDetails>,
    type: TipoTransaccion,
    chartData: List<PieChartData>,
    onTransactionClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            BalanceCard(
                balancePrimario = balanceVes,
                balanceSecundario = balanceUsd,
                symbolPrimario = primaryCurrencySymbol,
                symbolSecundario = secondaryCurrencySymbol,
                type = type
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                usedCurrenciesInMonth.forEach { currencySymbol ->
                    FilterChip(
                        selected = selectedCurrency == currencySymbol,
                        onClick = { onCurrencySelected(currencySymbol) },
                        label = { Text(currencySymbol) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (chartData.isNotEmpty()) {
            item {
                val chartTitle = if (type == TipoTransaccion.GASTO) "Distribución de Gastos del Mes" else "Distribución de Ingresos del Mes"
                PieChartCard(chartData = chartData, title = "$chartTitle (en $selectedCurrency)")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (transactions.isEmpty()) {
            item {
                EmptyState(modifier = Modifier.padding(bottom = 60.dp))
            }
        } else {
            item {
                RecentTransactions(
                    transactions = transactions,
                    onTransactionClick = onTransactionClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
        }
    }
}