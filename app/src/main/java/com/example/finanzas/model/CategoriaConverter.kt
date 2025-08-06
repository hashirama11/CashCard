package com.example.finanzas.model

import androidx.room.TypeConverter


class CategoriaConverter {
    @TypeConverter
    fun fromCategoria(categoria: Categorias): String = categoria.name

    @TypeConverter
    fun toCategoria(nombre: String): Categorias = Categorias.valueOf(nombre)
}