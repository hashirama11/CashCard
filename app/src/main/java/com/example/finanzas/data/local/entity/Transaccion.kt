package com.example.finanzas.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    indices = [Index(value = ["categoriaId"])]
)
data class Transaccion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val monto: Double,
    val moneda: String,
    val descripcion: String,
    val fecha: Date,
    val tipo: String,
    val estado: String,
    val categoriaId: Int?,
    // --- NUEVO CAMPO PARA FECHA DE NOTIFICACIÓN ---
    val fechaConcrecion: Date? = null
)