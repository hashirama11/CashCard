package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.PieChartData
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.TransactionWithDetails

@Composable
fun DashboardContent(
    balance: Double,
    transactions: List<TransactionWithDetails>,
    type: TipoTransaccion,
    chartData: List<PieChartData>, // <-- DATO NUEVO
    onTransactionClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BalanceCard(balance = balance, type = type)
        Spacer(modifier = Modifier.height(16.dp))

        // Mostramos el gráfico solo en la pestaña de Gastos
        if (type == TipoTransaccion.GASTO) {
            PieChartCard(chartData = chartData)
            Spacer(modifier = Modifier.height(16.dp))
        }

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