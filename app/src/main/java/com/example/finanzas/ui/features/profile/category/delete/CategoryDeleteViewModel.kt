package com.example.finanzas.ui.features.profile.category.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.model.categoria.CategoriaEntity
import com.example.finanzas.repository.categoria.CategoriaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryDeleteViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    val categorias: StateFlow<List<CategoriaEntity>> =
        categoriaRepository.obtenerTodasLasCategoriasFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun eliminarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            categoriaRepository.eliminarCategoria(categoria)
        }
    }
}

