package com.example.finanzas.ui.features.ListOperation.components

import com.example.finanzas.model.categoria.Categoria

data class ListUiState(
    val categorias: List<Categoria> = emptyList(),
    val selectedCategory: Categoria? = null
)
