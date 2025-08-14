package com.example.finanzas.model.categoria

import androidx.room.TypeConverter


class CategoriaConverter {
    @TypeConverter
    fun fromCategoria(categoria: CategoriaEntity): String = categoria.name

    @TypeConverter
    fun toCategoria(nombre: String): CategoriaEntity = CategoriaEntity.valueOf(nombre)
}