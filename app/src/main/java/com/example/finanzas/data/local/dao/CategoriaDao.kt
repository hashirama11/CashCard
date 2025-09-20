package com.example.finanzas.data.local.dao

import androidx.room.*
import com.example.finanzas.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: Categoria)

    @Delete
    suspend fun deleteCategoria(categoria: Categoria)

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun getAllCategorias(): Flow<List<Categoria>>

    @Query("SELECT * FROM categorias WHERE id = :id")
    fun getCategoriaById(id: Int): Flow<Categoria?>

    @Query("SELECT * FROM categorias WHERE tipo = 'GASTO' ORDER BY nombre ASC")
    suspend fun getCategoriasGastos(): List<Categoria>

    @Query("SELECT * FROM categorias WHERE tipo = 'INGRESO' LIMIT 1")
    suspend fun getIncomeCategory(): Categoria?

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun getCategoriaByIdSinc(id: Int): Categoria?
}