package com.example.finanzas.ui.add_transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.Moneda
import com.example.finanzas.model.TipoTransaccion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    val descriptionLabel = when (state.selectedTransactionType) {
        TipoTransaccion.INGRESO -> "Descripción (ej: Salario, Venta)"
        TipoTransaccion.GASTO -> "Descripción (ej: Café en la panadería)"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Editar Transacción" else "Añadir Transacción") },
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
                    viewModel.saveTransaction()
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                Text(if (state.isEditing) "Guardar Cambios" else "Guardar Transacción")
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
            TabRow(selectedTabIndex = state.selectedTransactionType.ordinal) {
                Tab(
                    selected = state.selectedTransactionType == TipoTransaccion.INGRESO,
                    onClick = { viewModel.onTransactionTypeSelected(TipoTransaccion.INGRESO) },
                    text = { Text("Ingreso") }
                )
                Tab(
                    selected = state.selectedTransactionType == TipoTransaccion.GASTO,
                    onClick = { viewModel.onTransactionTypeSelected(TipoTransaccion.GASTO) },
                    text = { Text("Gasto") }
                )
            }

            // --- NUEVO SELECTOR DE MONEDA ---
            Row(modifier = Modifier.fillMaxWidth()) {
                Moneda.values().forEach { moneda ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                1.dp,
                                if (state.selectedCurrency == moneda) MaterialTheme.colorScheme.primary else Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .background(
                                if (state.selectedCurrency == moneda) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
                            )
                            .clickable { viewModel.onCurrencySelected(moneda) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(moneda.name, color = if (state.selectedCurrency == moneda) MaterialTheme.colorScheme.primary else Color.Gray)
                    }
                    if (moneda != Moneda.values().last()) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }


            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.onAmountChange(it) },
                label = { Text("Monto") },
                leadingIcon = { Text(state.selectedCurrency.simbolo, style = MaterialTheme.typography.bodyLarge) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text(descriptionLabel) },
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
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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
                }
            }

            // --- NUEVO INTERRUPTOR DE ESTADO PENDIENTE ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("¿Marcar como pendiente?", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = state.isPending,
                    onCheckedChange = { viewModel.onPendingStatusChange(it) }
                )
            }
        }
    }
}