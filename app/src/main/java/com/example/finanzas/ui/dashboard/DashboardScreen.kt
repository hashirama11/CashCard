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
    val dynamicTabCount = state.dashboardPanels.size
    val fixedTabs = listOf("Ahorro", "Resumen")
    val totalTabs = dynamicTabCount * 2 + fixedTabs.size // Each currency has Ingreso/Gasto

    val pagerState = rememberPagerState(pageCount = { totalTabs })
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
            // Dynamic TabRow
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                state.dashboardPanels.forEachIndexed { index, panel ->
                    Tab(
                        selected = pagerState.currentPage == index * 2,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index * 2) } },
                        text = { Text("${panel.currencySymbol} Ingreso") }
                    )
                    Tab(
                        selected = pagerState.currentPage == index * 2 + 1,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index * 2 + 1) } },
                        text = { Text("${panel.currencySymbol} Gasto") }
                    )
                }
                fixedTabs.forEachIndexed { index, title ->
                    val pageIndex = dynamicTabCount * 2 + index
                    Tab(
                        selected = pagerState.currentPage == pageIndex,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(pageIndex) } },
                        text = { Text(title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                if (page < dynamicTabCount * 2) {
                    val panelIndex = page / 2
                    val type = if (page % 2 == 0) TipoTransaccion.INGRESO else TipoTransaccion.GASTO
                    val panel = state.dashboardPanels[panelIndex]

                    if (type == TipoTransaccion.INGRESO) {
                        DashboardContent(
                            balanceVes = panel.totalIncome,
                            balanceUsd = 0.0, // Not used in this dynamic view
                            ahorroAcumulado = state.ahorroAcumulado,
                            transactions = panel.incomeTransactions,
                            type = TipoTransaccion.INGRESO,
                            chartData = panel.incomeChartData,
                            onTransactionClick = onTransactionClick,
                            onSeeAllClick = onSeeAllClick
                        )
                    } else {
                        DashboardContent(
                            balanceVes = -panel.totalExpenses,
                            balanceUsd = 0.0, // Not used in this dynamic view
                            ahorroAcumulado = state.ahorroAcumulado,
                            transactions = panel.expenseTransactions,
                            type = TipoTransaccion.GASTO,
                            chartData = panel.expenseChartData,
                            onTransactionClick = onTransactionClick,
                            onSeeAllClick = onSeeAllClick
                        )
                    }
                } else {
                    when (page - dynamicTabCount * 2) {
                        0 -> SavingsDashboardContent(
                            totalAhorrosVes = state.totalAhorrosVes,
                            totalAhorrosUsd = state.totalAhorrosUsd,
                            primaryCurrencySymbol = state.primaryCurrencySymbol,
                            secondaryCurrencySymbol = state.secondaryCurrencySymbol,
                            selectedSavingsCurrency = state.selectedSavingsCurrency,
                            onCurrencySelected = { viewModel.onSavingsCurrencySelected(it) },
                            savingsChartData = state.savingsChartData,
                            transactions = state.dashboardPanels.flatMap { it.incomeTransactions + it.expenseTransactions }
                                .filter { it.transaccion.tipo == TipoTransaccion.AHORRO.name },
                            onTransactionClick = onTransactionClick
                        )
                        1 -> MonthlySummaryChart(monthlySummary = state.monthlySummary)
                    }
                }
            }
        }
    }
}