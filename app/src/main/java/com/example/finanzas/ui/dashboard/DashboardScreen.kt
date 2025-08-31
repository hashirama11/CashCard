package com.example.finanzas.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.balance.components.SummaryCard
import com.example.finanzas.ui.components.LoadingIndicator
import com.example.finanzas.ui.dashboard.components.DashboardTopAppBar
import com.example.finanzas.ui.dashboard.components.EmptyState
import com.example.finanzas.ui.dashboard.components.PieChartCard
import com.example.finanzas.ui.dashboard.components.RecentTransactions
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat
import java.util.Currency

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
    val numberFormat = remember(state.selectedCurrency) {
        state.selectedCurrency?.let {
            NumberFormat.getCurrencyInstance().apply {
                currency = Currency.getInstance(it.nombre)
                maximumFractionDigits = 2
            }
        }
    }

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
        if (state.isLoading || numberFormat == null) {
            LoadingIndicator()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.usedCurrenciesInMonth) { moneda ->
                            FilterChip(
                                selected = state.selectedCurrency == moneda,
                                onClick = { viewModel.onCurrencySelected(moneda) },
                                label = { Text(moneda.nombre) }
                            )
                        }
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SummaryCard(
                            title = "Ingresos del Mes",
                            amount = numberFormat.format(state.totalIngresos),
                            color = AccentGreen,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Gastos del Mes",
                            amount = numberFormat.format(state.totalGastos),
                            color = AccentRed,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (state.incomeChartData.isNotEmpty()) {
                    item {
                        PieChartCard(
                            chartData = state.incomeChartData,
                            title = "Distribución de Ingresos (en ${state.selectedCurrency?.nombre})"
                        )
                    }
                }

                if (state.expenseChartData.isNotEmpty()) {
                    item {
                        PieChartCard(
                            chartData = state.expenseChartData,
                            title = "Distribución de Gastos (en ${state.selectedCurrency?.nombre})"
                        )
                    }
                }

                item {
                    val transactionsToShow = remember(state.transactions) {
                        state.transactions
                            .filter { it.transaccion.tipo != TipoTransaccion.AHORRO.name }
                            .sortedByDescending { it.transaccion.fecha }
                    }

                    if (transactionsToShow.isEmpty()) {
                        EmptyState(modifier = Modifier.padding(vertical = 32.dp))
                    } else {
                        RecentTransactions(
                            transactions = transactionsToShow,
                            onTransactionClick = onTransactionClick,
                            onSeeAllClick = onSeeAllClick
                        )
                    }
                }
            }
        }
    }
}