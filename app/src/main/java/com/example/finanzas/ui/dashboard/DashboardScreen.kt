package com.example.finanzas.ui.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.dashboard.components.DashboardContent
import com.example.finanzas.ui.dashboard.components.DashboardTabRow
import com.example.finanzas.ui.dashboard.components.DashboardTopAppBar
import com.example.finanzas.ui.dashboard.components.MonthlySummaryChart
import com.example.finanzas.ui.dashboard.components.SavingsChart
import com.example.finanzas.ui.dashboard.components.SavingsDashboardContent
import com.example.finanzas.ui.theme.AccentGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onAddTransaction: () -> Unit,
    onTransactionClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit,
    onPurchaseHistoryClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DashboardTabRow(
                selectedTabIndex = pagerState.currentPage,
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> DashboardContent(
                        balanceVes = state.totalIngresosVes,
                        balanceUsd = state.totalIngresosUsd,
                        ahorroAcumulado = state.ahorroAcumulado, // <-- Pasamos el ahorro
                        onTransactionClick = onTransactionClick,
                        transactions = state.transactionsWithDetails.filter { it.transaccion.tipo == TipoTransaccion.INGRESO.name },
                        type = TipoTransaccion.INGRESO,
                        chartData = state.incomeChartData,
                        onSeeAllClick = onSeeAllClick
                    )
                    1 -> DashboardContent(
                        // Para gastos, el balance es negativo
                        balanceVes = -state.totalGastosVes,
                        balanceUsd = -state.totalGastosUsd,
                        ahorroAcumulado = state.ahorroAcumulado, // <-- Pasamos el ahorro
                        onTransactionClick = onTransactionClick,
                        transactions = state.transactionsWithDetails.filter { it.transaccion.tipo == TipoTransaccion.GASTO.name },
                        type = TipoTransaccion.GASTO,
                        chartData = state.expenseChartData,
                        onSeeAllClick = onSeeAllClick
                    )
                    2 -> MonthlySummaryChart(monthlySummary = state.monthlySummary)
                    3 -> SavingsDashboardContent(
                        totalAhorrosVes = state.totalAhorrosVes,
                        totalAhorrosUsd = state.totalAhorrosUsd,
                        primaryCurrencySymbol = state.primaryCurrencySymbol,
                        secondaryCurrencySymbol = state.secondaryCurrencySymbol,
                        selectedSavingsCurrency = state.selectedSavingsCurrency,
                        onCurrencySelected = { viewModel.onSavingsCurrencySelected(it) },
                        savingsChartData = state.savingsChartData,
                        transactions = state.transactionsWithDetails.filter { it.transaccion.tipo == TipoTransaccion.AHORRO.name },
                        onTransactionClick = onTransactionClick
                    )
                }
            }
        }
    }
}