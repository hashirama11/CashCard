package com.example.finanzas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val icono: String,
    val tipo: String, // "INGRESO" o "GASTO" -> ¡Este es el cambio clave!
    val esPersonalizada: Boolean = false // Cambiado a false por defecto para las estándar
)