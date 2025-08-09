package com.example.finanzas.ui.features.ListOperation

import androidx.annotation.DrawableRes
import com.example.finanzas.R
import com.example.finanzas.model.gasto.GastoEntity
import java.time.LocalDate

val SAMPLE_GASTO = GastoEntity().apply {
    id = 1
    descripcion = "Compra de supermercado"
    categoria = "Alimentos"
    monto = 1500.50
    fecha = LocalDate.of(2025, 8, 9)
}

data class OperactionUiState(
    @DrawableRes val icon: Int = R.drawable.shopping,
    val gastos : List<GastoEntity> = listOf<GastoEntity>(SAMPLE_GASTO)
)
