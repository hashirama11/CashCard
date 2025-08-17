package com.example.finanzas.ui.features.transacciones.cumplidas

data class TransaccionCumplidaUiState(
    val categoria : String = "",
    val monto : Double = 0.0,
    val fecha : String = "",
    val icono : Int = 0,
)
