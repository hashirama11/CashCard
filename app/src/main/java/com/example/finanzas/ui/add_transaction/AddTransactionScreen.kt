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
    val state by viewModel.state.collectAsState()
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val selectedType = state.selectedTransactionType

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
                    viewModel.saveTransaction(amountDouble, description, selectedType, state.selectedCategory)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp)
            ) {
                Text("Guardar Transacción")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TabRow(selectedTabIndex = selectedType.ordinal) {
                Tab(
                    selected = selectedType == TipoTransaccion.INGRESO,
                    onClick = { viewModel.onTransactionTypeSelected(TipoTransaccion.INGRESO) },
                    text = { Text("Ingreso") }
                )
                Tab(
                    selected = selectedType == TipoTransaccion.GASTO,
                    onClick = { viewModel.onTransactionTypeSelected(TipoTransaccion.GASTO) },
                    text = { Text("Gasto") }
                )
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto (ej: 150.50)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción (ej: Café en la panadería)") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.selectedCategory?.nombre ?: "Seleccionar categoría",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.filteredCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.nombre) },
                            onClick = {
                                viewModel.onCategorySelected(category)
                                expanded = false
                            }
                        )
                    }
                    if (state.filteredCategories.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No hay categorías para este tipo") },
                            onClick = { },
                            enabled = false
                        )
                    }
                }
            }
        }
    }
}