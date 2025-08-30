package com.example.finanzas.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.dashboard.components.*
import com.example.finanzas.ui.theme.AccentGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onAddTransaction: () -> Unit,
    onTransactionClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit,
    onPurchaseHistoryClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { DashboardTopAppBar(userName = state.userName, onPurchaseHistoryClick = onPurchaseHistoryClick) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddTransaction() },
                containerColor = AccentGreen
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Añadir Transacción"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DashboardContent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    balanceVes = state.totalIngresosPrimario,
                    balanceUsd = state.totalIngresosSecundario,
                    primaryCurrencySymbol = state.primaryCurrencySymbol,
                    secondaryCurrencySymbol = state.secondaryCurrencySymbol,
                    usedCurrenciesInMonth = state.usedCurrenciesInMonth,
                    selectedCurrency = state.selectedDashboardCurrency,
                    onCurrencySelected = { viewModel.onDashboardCurrencySelected(it) },
                    transactions = state.incomeTransactions,
                    type = TipoTransaccion.INGRESO,
                    chartData = state.incomeChartData,
                    onTransactionClick = onTransactionClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
            item {
                DashboardContent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    balanceVes = -state.totalGastosPrimario,
                    balanceUsd = -state.totalGastosSecundario,
                    primaryCurrencySymbol = state.primaryCurrencySymbol,
                    secondaryCurrencySymbol = state.secondaryCurrencySymbol,
                    usedCurrenciesInMonth = state.usedCurrenciesInMonth,
                    selectedCurrency = state.selectedDashboardCurrency,
                    onCurrencySelected = { viewModel.onDashboardCurrencySelected(it) },
                    transactions = state.expenseTransactions,
                    type = TipoTransaccion.GASTO,
                    chartData = state.expenseChartData,
                    onTransactionClick = onTransactionClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
            item {
                SavingsDashboardContent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    totalAhorrosVes = state.totalAhorrosPrimario,
                    totalAhorrosUsd = state.totalAhorrosSecundario,
                    primaryCurrencySymbol = state.primaryCurrencySymbol,
                    secondaryCurrencySymbol = state.secondaryCurrencySymbol,
                    selectedSavingsCurrency = state.selectedSavingsCurrency,
                    onCurrencySelected = { viewModel.onSavingsCurrencySelected(it) },
                    savingsChartData = state.savingsChartData,
                    transactions = state.savingsTransactions,
                    onTransactionClick = onTransactionClick
                )
            }
            item {
                MonthlySummaryChart(monthlySummary = state.monthlySummary)
            }
        }
    }
}