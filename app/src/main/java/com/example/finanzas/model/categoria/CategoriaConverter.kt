package com.example.finanzas.model.categoria

import androidx.room.TypeConverter


class CategoriaConverter {
    @TypeConverter
    fun fromCategoria(categoria: Categorias): String = categoria.name

    @TypeConverter
    fun toCategoria(nombre: String): Categorias = Categorias.valueOf(nombre)
}