package com.example.finanzas.ui.add_transaction

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
import com.example.finanzas.model.TipoTransaccion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TipoTransaccion.GASTO) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Transacción") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    viewModel.saveTransaction(amountDouble, description, selectedType)
                    onBack() // Vuelve a la pantalla anterior después de guardar
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                Text("Guardar Transacción")
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
            // Selector de Tipo (Ingreso / Gasto)
            TabRow(selectedTabIndex = selectedType.ordinal) {
                Tab(
                    selected = selectedType == TipoTransaccion.INGRESO,
                    onClick = { selectedType = TipoTransaccion.INGRESO },
                    text = { Text("Ingreso") }
                )
                Tab(
                    selected = selectedType == TipoTransaccion.GASTO,
                    onClick = { selectedType = TipoTransaccion.GASTO },
                    text = { Text("Gasto") }
                )
            }

            // Campo de Monto
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto (ej: 150.50)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Campo de Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción (ej: Café en la panadería)") },
                modifier = Modifier.fillMaxWidth()
            )

            // TODO: Añadir selector de categoría aquí
        }
    }
}