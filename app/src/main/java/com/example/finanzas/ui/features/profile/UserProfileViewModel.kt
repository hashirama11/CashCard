package com.example.finanzas.ui.features.profile

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
class UserProfileViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository,
    private val gastoDao: GastoDao
) : ViewModel() {

    // Estado del formulario
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _icono = MutableStateFlow(0)
    val icono: StateFlow<Int> = _icono

    fun onNombreChanged(nuevoNombre: String) {
        _nombre.value = nuevoNombre
    }

    fun onIconoChanged(nuevoIcono: Int) {
        _icono.value = nuevoIcono
    }

    // 🔹 Crear categoría
    fun crearCategoria() {
        viewModelScope.launch {
            val nuevaCategoria = CategoriaEntity(
                nombre = _nombre.value,
                icono = _icono.value
            )
            categoriaRepository.crearCategoria(nuevaCategoria)

            // limpiar campo después de guardar
            _nombre.value = ""
            _icono.value = 0
        }
    }

    // 🔹 Actualizar categoría
    fun actualizarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            categoriaRepository.actualizarCategoria(categoria)
        }
    }

    // 🔹 Eliminar categoría
    fun eliminarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            categoriaRepository.eliminarCategoria(categoria)
        }
    }

    // 🔹 Restablecer la BD de gastos (borrar todos los registros)
    fun resetearGastos() {
        viewModelScope.launch {
            gastoDao.eliminarTodosLosGastos()
        }
    }

    // 🔹 Obtener todas las categorías en vivo
    val categorias: StateFlow<List<CategoriaEntity>> =
        categoriaRepository.obtenerTodasLasCategoriasFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
