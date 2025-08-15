package com.example.finanzas.ui.features.statistics_home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StatisticsRoute(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()

    StatisticsScreen(
        state = uiState
    )
}
