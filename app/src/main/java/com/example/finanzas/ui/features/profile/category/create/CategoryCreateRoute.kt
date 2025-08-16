package com.example.finanzas.ui.features.profile.category.create

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.ui.screen.AppNavigationBar

@Composable
fun CategoryCreateRoute(
    onHomeClick: () -> Unit,
    onStatsClick: () -> Unit,
    onUsersClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: CategoriaCreateViewModel = hiltViewModel()
) {
    val nombre by viewModel.nombre.collectAsState()
    val icono by viewModel.icono.collectAsState()

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
        CategoryCreateScreen(
            modifier = Modifier.padding(padding),
            nombre = nombre,
            onNombreChanged = viewModel::onNombreChanged,
            icono = icono,
            onGuardarClick = viewModel::guardarCategoria
        )
    }
}

