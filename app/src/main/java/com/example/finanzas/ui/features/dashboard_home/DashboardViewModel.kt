package com.example.finanzas.ui.features.dashboard_home

import androidx.lifecycle.ViewModel
import com.example.finanzas.repository.gasto.GastoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val gastoRepository: GastoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())

    val state: StateFlow<DashboardUiState> = _state

    fun obtenerSaldoActual() {
        viewModelScope.launch {
            gastoRepository.obtenerTotalGastos().collect { saldo ->
                _state.value = _state.value.copy(saldoActual = saldo ?: 0.0)
            }
        }

    }
}