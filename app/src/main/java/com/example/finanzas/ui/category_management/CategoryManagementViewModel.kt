package com.example.finanzas.ui.category_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.IconosEstandar
import com.example.finanzas.model.TipoTransaccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryManagementViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    val categories = repository.getAllCategorias()
        .map { list -> list.filter { it.esPersonalizada } } // Solo mostramos las personalizadas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCategory(name: String, type: TipoTransaccion, icon: IconosEstandar) {
        viewModelScope.launch {
            val newCategory = Categoria(
                nombre = name,
                tipo = type.name,
                icono = icon.name,
                esPersonalizada = true
            )
            repository.insertCategoria(newCategory)
        }
    }

    fun deleteCategory(category: Categoria) {
        viewModelScope.launch {
            repository.deleteCategoria(category)
        }
    }
}