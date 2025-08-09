package com.example.finanzas.ui.features.ListOperation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OperactionRoute(
    viewModel: OperationViewModel = hiltViewModel(),
){
    val state = viewModel.uiState.collectAsState()

    Column() {
        // 🔹 Lista de gastos
        state.value.gastos.forEach { gasto ->
            OperacionScreen(
                gasto = gasto,
                icon = state.value.icon
            )
        }
    }
}