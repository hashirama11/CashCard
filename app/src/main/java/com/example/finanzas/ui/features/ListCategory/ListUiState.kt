package com.example.finanzas.ui.features.ListCategory

import com.example.finanzas.model.categoria.Categorias

data class ListUiState(
    val categorias: List<Categorias> = emptyList(),
    val selectedCategory: Categorias? = null
)
