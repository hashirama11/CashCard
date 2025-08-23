package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails

@Composable
fun DashboardContent(
    balanceVes: Double,
    balanceUsd: Double,
    ahorroAcumulado: Double, // <-- NUEVO
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
            // Pasamos el nuevo valor a la tarjeta de balance
            BalanceCard(
                balanceVes = balanceVes,
                balanceUsd = balanceUsd,
                ahorroAcumulado = ahorroAcumulado,
                type = type
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (chartData.isNotEmpty()) {
            item {
                val chartTitle = if (type == TipoTransaccion.GASTO) "Distribución de Gastos del Mes" else "Distribución de Ingresos del Mes"
                PieChartCard(chartData = chartData, title = chartTitle)
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