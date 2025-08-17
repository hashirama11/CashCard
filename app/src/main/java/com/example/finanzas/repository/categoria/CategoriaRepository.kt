package com.example.finanzas.repository.categoria

import com.example.finanzas.model.categoria.CategoriaEntity
import kotlinx.coroutines.flow.Flow

interface CategoriaRepository {
    suspend fun crearCategoria(categoria: CategoriaEntity)
    suspend fun crearCategorias(categorias: List<CategoriaEntity>)
    suspend fun obtenerTodasLasCategorias(): List<CategoriaEntity>
    fun obtenerTodasLasCategoriasFlow(): Flow<List<CategoriaEntity>>
    suspend fun obtenerCategoriaPorId(id: Int): CategoriaEntity?
    suspend fun actualizarCategoria(categoria: CategoriaEntity)
    suspend fun eliminarCategoria(categoria: CategoriaEntity)

}
