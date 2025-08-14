package com.example.finanzas.ui.features.Form

import com.example.finanzas.model.categoria.CategoriaEntity

data class FormUiState(
    val descripcion : String = "",
    val categoriaSeleccionada: CategoriaEntity = CategoriaEntity.ALIMENTACION,
    val monto: String = "",
)
