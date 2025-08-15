package com.example.finanzas.ui.features.statistics_home

import androidx.compose.ui.text.style.TextDecoration.Companion.combine
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.repository.categoria.CategoriaRepository
import com.example.finanzas.repository.gasto.GastoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val gastoRepository: GastoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsUiState())
    val state: StateFlow<StatisticsUiState> = _state

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            categoriaRepository
                .obtenerTodasLasCategoriasFlow()
                .flatMapLatest { categorias ->
                    if (categorias.isEmpty()) {
                        flowOf(emptyList())
                    } else {
                        combine(
                            categorias.map { categoria ->
                                gastoRepository
                                    .obtenerTotalGastosPorCategoria(categoria.nombre)
                                    .map { total ->
                                        CategoriaConGasto(
                                            categoria = categoria.nombre,
                                            total = total ?: 0.0
                                        )
                                    }
                            }
                        ) { it.toList() }
                    }
                }
                .collect { listaFinal ->
                    _state.value = _state.value.copy(categoriasConGastos = listaFinal)
                }
        }
    }
}

data class CategoriaConGasto(
    val categoria: String,
    val total: Double
)
