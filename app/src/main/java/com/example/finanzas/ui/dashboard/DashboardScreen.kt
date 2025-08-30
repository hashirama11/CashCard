package com.example.finanzas.ui.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.dashboard.components.*
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
            val titles = listOf("Ingresos", "Gastos", "Ahorro", "Resumen")
            TabRow(selectedTabIndex = pagerState.currentPage) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> DashboardContent(
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
                    1 -> DashboardContent(
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
                    2 -> SavingsDashboardContent(
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
                    3 -> MonthlySummaryChart(monthlySummary = state.monthlySummary)
                }
            }
        }
    }
}