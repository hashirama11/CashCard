package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Column
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
    balanceVes: Double, // <-- Parámetro modificado
    balanceUsd: Double, // <-- Parámetro nuevo
    transactions: List<TransactionWithDetails>,
    type: TipoTransaccion,
    chartData: List<PieChartData>,
    onTransactionClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BalanceCard(balanceVes = balanceVes, balanceUsd = balanceUsd, type = type)
        Spacer(modifier = Modifier.height(16.dp))

        if (chartData.isNotEmpty()) {
            val chartTitle = if (type == TipoTransaccion.GASTO) "Distribución de Gastos" else "Distribución de Ingresos"
            PieChartCard(chartData = chartData, title = chartTitle)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (transactions.isEmpty()) {
            EmptyState(modifier = Modifier.padding(bottom = 60.dp))
        } else {
            RecentTransactions(
                transactions = transactions,
                onTransactionClick = onTransactionClick,
                onSeeAllClick = onSeeAllClick
            )
        }
    }
}