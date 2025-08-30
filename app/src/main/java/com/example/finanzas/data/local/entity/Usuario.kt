package com.example.finanzas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey
    val id: Int = 1,
    val nombre: String,
    val email: String?,
    val fechaNacimiento: Long?,
    val monedaPrincipal: String,
    val monedaSecundaria: String?,
    val tema: String,
    val onboardingCompletado: Boolean = false,
    // --- NUEVOS CAMPOS ---
    val ahorroAcumulado: Double = 0.0, // Para guardar el saldo arrastrado
    val objetivoAhorroMensual: Double = 0.0, // Meta de ahorro para el mes
    val fechaUltimoCierre: Long = Date().time // Para saber cuándo se hizo el último cierre
)