package com.example.finanzas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monedas")
data class Moneda(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val simbolo: String,
    val tasa_conversion: Double = 1.0 // Tasa de conversión respecto a una moneda base (ej. USD)
)
