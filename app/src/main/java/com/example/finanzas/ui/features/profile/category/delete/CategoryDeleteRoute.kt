package com.example.finanzas.ui.features.profile.category.delete

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.categoria.CategoriaEntity
import com.example.finanzas.ui.screen.AppNavigationBar

@Composable
fun CategoryDeleteRoute(
    onHomeClick: () -> Unit,
    onStatsClick: () -> Unit,
    onUsersClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: CategoryDeleteViewModel = hiltViewModel()
) {
    val categorias by viewModel.categorias.collectAsState()
    var categoriaSeleccionada by remember { mutableStateOf<CategoriaEntity?>(null) }

    Scaffold(
        bottomBar = {
            AppNavigationBar(
                onHomeClick = onHomeClick,
                onStatsClick = onStatsClick,
                onUsersClick = onUsersClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { padding ->
        CategoryDeleteScreen(
            modifier = Modifier.padding(padding),
            categorias = categorias,
            categoriaSeleccionada = categoriaSeleccionada,
            onCategoriaClick = { categoriaSeleccionada = it },
            onConfirmDelete = {
                categoriaSeleccionada?.let { viewModel.eliminarCategoria(it) }
                categoriaSeleccionada = null
            },
            onDismissDialog = {
                categoriaSeleccionada = null
            }
        )
    }
}
