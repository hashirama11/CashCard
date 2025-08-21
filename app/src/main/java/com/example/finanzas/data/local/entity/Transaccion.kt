package com.example.finanzas.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index // <-- ASEGÚRATE DE IMPORTAR ESTO
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transacciones",
    foreignKeys = [
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    // --- LÍNEA AÑADIDA PARA LA MEJORA DE RENDIMIENTO ---
    indices = [Index(value = ["categoriaId"])]
)
data class Transaccion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val monto: Double,
    val moneda: String, // Ej: "USD", "VES"
    val descripcion: String,
    val fecha: Date,
    val tipo: String, // "INGRESO" o "GASTO"
    val estado: String, // "CONCRETADO" o "PENDIENTE"
    val categoriaId: Int?
)