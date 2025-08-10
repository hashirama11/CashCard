package com.example.finanzas.ui.features.ListOperation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

        // Mostrar lista de gastos con icono de su categoría
        state.gastos.forEach { gasto ->
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

