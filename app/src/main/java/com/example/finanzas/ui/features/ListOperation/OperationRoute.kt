package com.example.finanzas.ui.features.ListOperation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import com.example.finanzas.model.categoria.Categoria
import com.example.finanzas.ui.features.ListOperation.components.CategoryRail

@Composable
fun OperactionRoute(
    selectedCategory: Categoria?,
    onCategorySelected: (Categoria?) -> Unit,
    viewModel: OperationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column {
        Spacer(modifier = Modifier.padding(6.dp))
        CategoryRail(
            selected = selectedCategory,
            onCategorySelected = { categoria ->
                onCategorySelected(categoria)
                if (categoria == null) {
                    viewModel.obtenerGastos()
                } else {
                    viewModel.obtenerGastosPorCategoria(categoria.name)
                }
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))

        // Mostrar lista de gastos con icono de su categoría
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(state.gastos) { gasto ->
                val categoriaEnum = gasto.categoria?.let { nombre ->
                    Categoria.values().find { it.name == nombre }
                }

                OperacionScreen(
                    gasto = gasto,
                    icon = categoriaEnum?.icono ?: R.drawable.shopping
                )
            }
        }

    }
}

