package com.example.finanzas.ui.features.Form

import com.example.finanzas.model.categoria.Categoria

data class FormUiState(
    val descripcion : String = "",
    val categoriaSeleccionada: Categoria = Categoria.ALIMENTACION,
    val monto: String = "",
)
