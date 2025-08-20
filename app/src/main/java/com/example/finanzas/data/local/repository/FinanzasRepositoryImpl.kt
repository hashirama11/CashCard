package com.example.finanzas.data.repository

import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Transaccion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.finanzas.data.local.entity.Usuario

class FinanzasRepositoryImpl @Inject constructor(
    private val transaccionDao: TransaccionDao,
    private val categoriaDao: CategoriaDao,
    private val usuarioDao: UsuarioDao
) : FinanzasRepository {

    override fun getAllTransacciones(): Flow<List<Transaccion>> {
        return transaccionDao.getAllTransacciones()
    }

    override suspend fun insertTransaccion(transaccion: Transaccion) {
        transaccionDao.insertTransaccion(transaccion)
    }

    override fun getAllCategorias(): Flow<List<Categoria>> {
        return categoriaDao.getAllCategorias()
    }

    override suspend fun insertCategoria(categoria: Categoria) {
        categoriaDao.insertCategoria(categoria)
    }

    override fun getUsuario(): Flow<Usuario?> {
        return usuarioDao.getUsuario()
    }

    override suspend fun upsertUsuario(usuario: Usuario) {
        usuarioDao.upsertUsuario(usuario)
    }
}