package com.example.finanzas.ui.balance

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.ui.balance.components.MonthlyFlowChart
import com.example.finanzas.ui.balance.components.SavingsRateIndicator
import com.example.finanzas.ui.balance.components.SummaryCard
import com.example.finanzas.ui.components.LoadingIndicator
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat
import java.util.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(
    viewModel: BalanceViewModel = hiltViewModel()
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
        topBar = {
            TopAppBar(
                title = { Text("Balance General", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
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
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(state.usedCurrencies) { moneda ->
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
                            title = "Ingresos Totales",
                            amount = numberFormat.format(state.totalIngresos),
                            color = AccentGreen,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Gastos Totales",
                            amount = numberFormat.format(state.totalGastos),
                            color = AccentRed,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    SavingsRateIndicator(
                        netBalance = state.balanceNeto,
                        savingsRate = state.tasaAhorro,
                        numberFormat = numberFormat
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Flujo de los Últimos 6 Meses (en ${state.selectedCurrency?.nombre ?: ""})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MonthlyFlowChart(monthlyFlows = state.monthlyFlows)
                }
            }
        }
    }
}