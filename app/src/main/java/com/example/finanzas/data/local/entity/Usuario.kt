package com.example.finanzas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey
    val id: Int = 1, // Siempre usaremos el ID 1 para el único usuario
    val nombre: String,
    val email: String?,
    val fechaNacimiento: Long?, // Guardaremos la fecha como Timestamp
    val monedaPrincipal: String, // Ej: "USD", "VES"
    val tema: String // "CLARO" o "OSCURO"
)