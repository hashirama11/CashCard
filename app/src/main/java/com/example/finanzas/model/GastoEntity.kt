package com.example.finanzas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "gastos")
class GastoEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var descripcion: String? = null
    var categoria: String? = null
    var monto: Double? = 0.0
    var fecha : LocalDate = LocalDate.now()
}