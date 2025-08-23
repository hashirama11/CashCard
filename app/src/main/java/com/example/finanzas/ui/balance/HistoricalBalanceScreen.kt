package com.example.finanzas.ui.balance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.ui.balance.components.MonthlyFlowChart
import com.example.finanzas.ui.balance.components.SavingsRateIndicator
import com.example.finanzas.ui.balance.components.SummaryCard
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricalBalanceScreen(
    viewModel: HistoricalBalanceViewModel = hiltViewModel() // <-- Usa el nuevo ViewModel
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Balance Histórico", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SummaryCard(
                        title = "Ingresos Totales",
                        amountVes = state.totalIngresosVes,
                        amountUsd = state.totalIngresosUsd,
                        color = AccentGreen,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Gastos Totales",
                        amountVes = state.totalGastosVes,
                        amountUsd = state.totalGastosUsd,
                        color = AccentRed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                SavingsRateIndicator(
                    netBalanceVes = state.balanceNetoVes,
                    netBalanceUsd = state.balanceNetoUsd,
                    savingsRate = state.tasaAhorro
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Flujo de los Últimos 6 Meses",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                MonthlyFlowChart(monthlyFlows = state.monthlyFlows)
            }
        }
    }
}