package com.example.finanzas.ui.features.centerButton

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.model.gasto.GastoEntity
import com.example.finanzas.repository.categoria.CategoriaRepository
import com.example.finanzas.repository.gasto.GastoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearGastoViewModel @Inject constructor(
    private val gastoRepository: GastoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CrearGastoUiState())
    val state: StateFlow<CrearGastoUiState> = _state

    init {
        cargarCategorias()
    }

    private fun cargarCategorias() {
        viewModelScope.launch {
            val categorias = categoriaRepository.obtenerTodasLasCategorias()
            _state.value = _state.value.copy(
                categoriasDisponibles = categorias.map { it.nombre }
            )
        }
    }

    fun onDescripcionChange(nuevaDescripcion: String) {
        _state.value = _state.value.copy(descripcion = nuevaDescripcion)
    }

    fun onCategoriaSeleccionada(categoria: String) {
        _state.value = _state.value.copy(categoriaSeleccionada = categoria)
    }

    fun onMontoChange(nuevoMonto: String) {
        _state.value = _state.value.copy(
            monto = nuevoMonto.toDoubleOrNull() ?: 0.0
        )
    }

    fun guardarGasto(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val gasto = GastoEntity().apply {
                descripcion = state.value.descripcion
                categoria = state.value.categoriaSeleccionada
                monto = state.value.monto
            }
            gastoRepository.crearGasto(gasto)
            onSuccess()
        }
    }
}

data class CrearGastoUiState(
    val descripcion: String = "",
    val categoriaSeleccionada: String = "",
    val monto: Double = 0.0,
    val categoriasDisponibles: List<String> = emptyList()
)
