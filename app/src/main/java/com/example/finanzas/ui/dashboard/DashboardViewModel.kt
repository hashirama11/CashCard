package com.example.finanzas.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getTransactions()
    }

    private fun getTransactions() {
        repository.getAllTransacciones()
            .onEach { transacciones ->
                val totalIngresos = transacciones
                    .filter { it.tipo == TipoTransaccion.INGRESO.name }
                    .sumOf { it.monto }

                val totalGastos = transacciones
                    .filter { it.tipo == TipoTransaccion.GASTO.name }
                    .sumOf { it.monto }

                _state.update {
                    it.copy(
                        transacciones = transacciones,
                        totalIngresos = totalIngresos,
                        totalGastos = totalGastos,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}