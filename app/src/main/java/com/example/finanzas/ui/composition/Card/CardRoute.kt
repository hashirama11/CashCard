package com.example.finanzas.ui.composition.Card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun CardRoute(
    viewModel: CardViewModel = hiltViewModel()
) {
    val total by viewModel.totalGastos.collectAsState()

    CardScreen(
        state = CardUiState(totalGastos = total)
    )
}
