package com.example.finanzas.ui.features.profile.category.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.model.categoria.CategoriaEntity
import com.example.finanzas.model.gasto.GastoDao
import com.example.finanzas.repository.categoria.CategoriaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriaUpdateViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _categoriaSeleccionada = MutableStateFlow<CategoriaEntity?>(null)
    val categoriaSeleccionada: StateFlow<CategoriaEntity?> = _categoriaSeleccionada

    val categorias: StateFlow<List<CategoriaEntity>> =
        categoriaRepository.obtenerTodasLasCategoriasFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onNombreChanged(nuevo: String) {
        _nombre.value = nuevo
    }

    fun onCategoriaSeleccionada(categoria: CategoriaEntity) {
        _categoriaSeleccionada.value = categoria
        _nombre.value = categoria.nombre
    }

    fun actualizarCategoria() {
        viewModelScope.launch {
            _categoriaSeleccionada.value?.let { categoria ->
                val actualizada = categoria.copy(
                    nombre = _nombre.value
                )
                categoriaRepository.actualizarCategoria(actualizada)
            }
        }
    }
}
