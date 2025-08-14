package com.example.finanzas.model.categoria

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
class CategoriaEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var nombre: String = ""
    var icono: Int = 0 // Es el ID del icono en Drawable

}