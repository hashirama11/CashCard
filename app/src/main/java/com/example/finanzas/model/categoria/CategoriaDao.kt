package com.example.finanzas.model.categoria

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    // Crear una categoría
    @Insert
    suspend fun crearCategoria(categoria: CategoriaEntity)

    // 🔹 Crear varias categorías (para precarga inicial)
    @Insert
    suspend fun crearCategorias(categorias: List<CategoriaEntity>)

    // Leer todas las categorías (una sola vez)
    @Query("SELECT * FROM categorias")
    suspend fun obtenerTodasLasCategorias(): List<CategoriaEntity>

    // Leer todas las categorías de forma reactiva
    @Query("SELECT * FROM categorias")
    fun obtenerTodasLasCategoriasFlow(): Flow<List<CategoriaEntity>>

    // Leer una categoría por su ID
    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun obtenerCategoriaPorId(id: Int): CategoriaEntity?

    // Actualizar una categoría
    @Update
    suspend fun actualizarCategoria(categoria: CategoriaEntity)

    // Eliminar una categoría
    @Delete
    suspend fun eliminarCategoria(categoria: CategoriaEntity)
}
