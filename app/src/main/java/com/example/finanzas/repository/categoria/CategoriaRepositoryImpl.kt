package com.example.finanzas.repository.categoria

import com.example.finanzas.model.categoria.CategoriaDao
import com.example.finanzas.model.categoria.CategoriaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriaRepositoryImpl @Inject constructor(
    private val categoriaDao: CategoriaDao
) : CategoriaRepository {

    override suspend fun crearCategoria(categoria: CategoriaEntity) {
        categoriaDao.crearCategoria(categoria)
    }

    override suspend fun obtenerTodasLasCategorias(): List<CategoriaEntity> {
        return categoriaDao.obtenerTodasLasCategorias()
    }

    override suspend fun crearCategorias(categorias: List<CategoriaEntity>) { // 👈 nuevo
        categoriaDao.crearCategorias(categorias)
    }

    override fun obtenerTodasLasCategoriasFlow(): Flow<List<CategoriaEntity>> {
        return categoriaDao.obtenerTodasLasCategoriasFlow()
    }

    override suspend fun obtenerCategoriaPorId(id: Int): CategoriaEntity? {
        return categoriaDao.obtenerCategoriaPorId(id)
    }

    override suspend fun actualizarCategoria(categoria: CategoriaEntity) {
        categoriaDao.actualizarCategoria(categoria)
    }

    override suspend fun eliminarCategoria(categoria: CategoriaEntity) {
        categoriaDao.eliminarCategoria(categoria)
    }
}
