package com.example.finanzas.ui.features.dashboard_home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DashboarRoute(
    viewModel: DashboardViewModel = hiltViewModel()
){
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit){
        viewModel.obtenerSaldoActual()
    }

    DashboardScreen(state = uiState)

}