package com.example.finanzas.ui.features.Card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.repository.gasto.GastoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: GastoRepository
) : ViewModel() {

    val totalGastos: StateFlow<Double> = repository
        .obtenerTotalGastos()
        .map { it ?: 0.0 } // Evita nulls, garantiza consistencia
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )
}
