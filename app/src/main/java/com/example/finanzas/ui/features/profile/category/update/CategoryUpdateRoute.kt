package com.example.finanzas.ui.features.profile.category.update


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.categoria.CategoriaEntity
import com.example.finanzas.ui.screen.AppNavigationBar

@Composable
fun CategoryUpdateRoute(
    onHomeClick: () -> Unit,
    onStatsClick: () -> Unit,
    onUsersClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: CategoriaUpdateViewModel = hiltViewModel()
) {
    val categorias by viewModel.categorias.collectAsState()
    val nombre by viewModel.nombre.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()

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
        CategoryUpdateScreen(
            categorias = categorias,
            categoriaSeleccionada = categoriaSeleccionada,
            onCategoriaClick = { viewModel.onCategoriaSeleccionada(it) },
            nombre = nombre,
            onNombreChanged = { viewModel.onNombreChanged(it) },
            onActualizarClick = { viewModel.actualizarCategoria() },
            modifier = Modifier.padding(padding)
        )
    }
}

