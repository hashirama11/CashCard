package com.example.finanzas.ui.features.centerButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CenterButtonRoute(
    onVolver: () -> Unit,
    viewModel: CrearGastoViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()

    CrearGastoScreen(
        state = uiState,
        onDescripcionChange = viewModel::onDescripcionChange,
        onCategoriaSeleccionada = viewModel::onCategoriaSeleccionada,
        onMontoChange = viewModel::onMontoChange,
        onGuardarClick = {
            viewModel.guardarGasto(onSuccess = onVolver)
        },
        onVolver = onVolver,
        onCumplimientoChange = {
            viewModel.onCumplimientoChange(it)
        }
    )
}
