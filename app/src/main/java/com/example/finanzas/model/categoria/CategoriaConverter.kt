package com.example.finanzas.model.categoria

import androidx.room.TypeConverter


class CategoriaConverter {
    @TypeConverter
    fun fromCategoria(categoria: Categoria): String = categoria.name

    @TypeConverter
    fun toCategoria(nombre: String): Categoria = Categoria.valueOf(nombre)
}