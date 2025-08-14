package com.example.finanzas.repository.categoria

import com.example.finanzas.model.categoria.CategoriaEntity

interface CategoriaRepository {
    suspend fun crearCategoria(categoria: CategoriaEntity)
    suspend fun obtenerTodasLasCategorias(): List<CategoriaEntity>
    suspend fun obtenerCategoriaPorId(id: Int): CategoriaEntity?
    suspend fun actualizarCategoria(categoria: CategoriaEntity)
    suspend fun eliminarCategoria(categoria: CategoriaEntity)

}