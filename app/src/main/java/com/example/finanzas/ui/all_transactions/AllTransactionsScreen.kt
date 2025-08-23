package com.example.finanzas.ui.all_transactions

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.dashboard.components.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
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
            // --- BUSCADOR Y FILTROS FUNCIONALES ---
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                label = { Text("Buscar por descripción o categoría...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
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
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.filteredTransactions, key = { it.transaccion.id }) { transaction ->
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