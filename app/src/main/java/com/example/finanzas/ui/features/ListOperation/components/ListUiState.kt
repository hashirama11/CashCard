package com.example.finanzas.ui.features.ListOperation.components

import com.example.finanzas.model.categoria.CategoriaEntity

data class ListUiState(
    val categorias: List<CategoriaEntity> = emptyList(),
    val selectedCategory: CategoriaEntity? = null
)
