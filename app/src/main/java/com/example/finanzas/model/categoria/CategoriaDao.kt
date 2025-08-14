package com.example.finanzas.model.categoria

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoriaDao {

    // Crear una categoria
    @Insert
    fun crearCategoria(categoria: CategoriaEntity)

    // Leer todas las categorias
    @Query("SELECT * FROM categorias")
    fun obtenerTodasLasCategorias(): List<CategoriaEntity>

    // Leer una categoria por su ID
    @Query("SELECT * FROM categorias WHERE id = :id")
    fun obtenerCategoriaPorId(id: Int): CategoriaEntity?

    // Actualizar una categoria
    @Update
    fun actualizarCategoria(categoria: CategoriaEntity)

    // Eliminar una categoria
    @Delete
    fun eliminarCategoria(categoria: CategoriaEntity)

}