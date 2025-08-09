package com.example.finanzas.ui.features.Form

import com.example.finanzas.model.categoria.Categorias

data class FormUiState(
    val descripcion : String = "",
    val categoriaSeleccionada: Categorias = Categorias.ALIMENTACION,
    val monto: String = "",
)
