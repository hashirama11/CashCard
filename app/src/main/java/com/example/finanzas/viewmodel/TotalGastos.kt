package com.example.finanzas.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.finanzas.repository.GastoRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class TotalGastoViewModel @Inject constructor(
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