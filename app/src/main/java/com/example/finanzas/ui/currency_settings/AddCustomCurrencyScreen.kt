package com.example.finanzas.ui.currency_settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomCurrencyScreen(
    viewModel: AddCustomCurrencyViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var symbol by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Moneda Personalizada") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la Moneda (ej: Peso Colombiano)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = symbol,
                onValueChange = { symbol = it },
                label = { Text("Símbolo (ej: COP)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = rate,
                onValueChange = { rate = it },
                label = { Text("Tasa de conversión a Dólar (ej: 4000)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val rateDouble = rate.toDoubleOrNull()
                    if (rateDouble != null) {
                        viewModel.addCurrency(name, symbol, rateDouble)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && symbol.isNotBlank() && rate.isNotBlank()
            ) {
                Text("Guardar Moneda")
            }
        }
    }
}
