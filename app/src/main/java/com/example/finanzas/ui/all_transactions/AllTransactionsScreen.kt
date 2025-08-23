package com.example.finanzas.ui.all_transactions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.dashboard.components.TransactionItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class) // Añadir ExperimentalFoundationApi
@Composable
fun AllTransactionsScreen(
    viewModel: AllTransactionsViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onTransactionClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todas las Transacciones") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                label = { Text("Buscar por descripción o categoría...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- FILTROS DE TIPO (Ingreso/Gasto) ---
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.filterType == TipoTransaccion.INGRESO,
                    onClick = { viewModel.onFilterTypeChange(TipoTransaccion.INGRESO) },
                    label = { Text("Ingresos") }
                )
                FilterChip(
                    selected = state.filterType == TipoTransaccion.GASTO,
                    onClick = { viewModel.onFilterTypeChange(TipoTransaccion.GASTO) },
                    label = { Text("Gastos") }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // --- NUEVO: FILTROS DE AGRUPACIÓN ---
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.selectedGrouping == GroupingType.DAILY,
                    onClick = { viewModel.onGroupingChange(GroupingType.DAILY) },
                    label = { Text("Día") }
                )
                FilterChip(
                    selected = state.selectedGrouping == GroupingType.WEEKLY,
                    onClick = { viewModel.onGroupingChange(GroupingType.WEEKLY) },
                    label = { Text("Semana") }
                )
                FilterChip(
                    selected = state.selectedGrouping == GroupingType.MONTHLY,
                    onClick = { viewModel.onGroupingChange(GroupingType.MONTHLY) },
                    label = { Text("Mes") }
                )
                FilterChip(
                    selected = state.selectedGrouping == GroupingType.YEARLY,
                    onClick = { viewModel.onGroupingChange(GroupingType.YEARLY) },
                    label = { Text("Año") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // --- MODIFICADO: LazyColumn con cabeceras flotantes ---
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                state.groupedTransactions.forEach { group ->
                    // Esta es la cabecera que se quedará fija arriba
                    stickyHeader {
                        Text(
                            text = group.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                                .padding(vertical = 8.dp)
                        )
                    }
                    // Estos son los items dentro de cada grupo
                    items(group.transactions, key = { it.transaccion.id }) { transaction ->
                        Box(modifier = Modifier.animateContentSize()) {
                            TransactionItem(
                                transactionDetails = transaction,
                                onClick = { onTransactionClick(transaction.transaccion.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}