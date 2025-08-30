package com.example.finanzas.ui.currency_settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySettingsScreen(
    viewModel: CurrencySettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onAddCustomCurrencyClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar("¡Monedas guardadas con éxito!")
                viewModel.resetSaveSuccess()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Ajustes de Moneda") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCustomCurrencyClick) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Moneda")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                CurrencySelector(
                    label = "Moneda Principal",
                    currencies = state.allCurrencies,
                    selectedCurrency = state.primaryCurrency,
                    onCurrencySelected = { viewModel.onPrimaryCurrencySelected(it) }
                )
                CurrencySelector(
                    label = "Moneda Secundaria (Opcional)",
                    currencies = state.allCurrencies,
                    selectedCurrency = state.secondaryCurrency,
                    onCurrencySelected = { viewModel.onSecondaryCurrencySelected(it) }
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { viewModel.saveCurrencySettings() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.primaryCurrency != null
                ) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencySelector(
    label: String,
    currencies: List<com.example.finanzas.data.local.entity.Moneda>,
    selectedCurrency: com.example.finanzas.data.local.entity.Moneda?,
    onCurrencySelected: (com.example.finanzas.data.local.entity.Moneda) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCurrency?.nombre ?: "Seleccionar...",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text("${currency.nombre} (${currency.simbolo})") },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}
