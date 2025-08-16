package com.example.finanzas.ui.features.profile.category.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.model.categoria.CategoriaEntity
import com.example.finanzas.repository.categoria.CategoriaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriaCreateViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _icono = MutableStateFlow(0) // por defecto, ninguno
    val icono: StateFlow<Int> = _icono

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onNombreChanged(nuevoNombre: String) {
        _nombre.value = nuevoNombre
        _error.value = null // limpiar error si el usuario cambia el texto
    }

    fun onIconoChanged(nuevoIcono: Int) {
        _icono.value = nuevoIcono
    }

    fun guardarCategoria() {
        viewModelScope.launch {
            val categoriasExistentes = categoriaRepository.obtenerTodasLasCategorias()

            // 🔹 Verificamos si ya existe una categoría con el mismo nombre (ignorando mayúsculas/minúsculas)
            val existe = categoriasExistentes.any { it.nombre.equals(_nombre.value, ignoreCase = true) }

            if (existe) {
                _error.value = "La categoría '${_nombre.value}' ya existe."
            } else {
                val nuevaCategoria = CategoriaEntity(
                    nombre = _nombre.value.trim(),
                    icono = _icono.value
                )
                categoriaRepository.crearCategoria(nuevaCategoria)

                // ✅ limpiar campos después de guardar
                _nombre.value = ""
                _icono.value = 0
                _error.value = null
            }
        }
    }
}
