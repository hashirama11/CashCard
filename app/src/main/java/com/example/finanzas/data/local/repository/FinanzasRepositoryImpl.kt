package com.example.finanzas.data.repository

import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FinanzasRepositoryImpl @Inject constructor(
    private val transaccionDao: TransaccionDao,
    private val categoriaDao: CategoriaDao,
    private val usuarioDao: UsuarioDao,
    private val monedaDao: MonedaDao
) : FinanzasRepository {

    override fun getAllTransacciones(): Flow<List<Transaccion>> = transaccionDao.getAllTransacciones()
import java.util.Calendar

class FinanzasRepositoryImpl @Inject constructor(
    private val transaccionDao: TransaccionDao,
    private val categoriaDao: CategoriaDao,
    private val usuarioDao: UsuarioDao,
    private val monedaDao: MonedaDao
) : FinanzasRepository {

    override fun getAllTransacciones(): Flow<List<Transaccion>> = transaccionDao.getAllTransacciones()
    override suspend fun insertTransaccion(transaccion: Transaccion) = transaccionDao.insertTransaccion(transaccion)
    override suspend fun updateTransaction(transaccion: Transaccion) = transaccionDao.updateTransaccion(transaccion)
    override fun getTransaccionById(id: Int): Flow<Transaccion?> = transaccionDao.getTransaccionById(id)
    override suspend fun deleteTransaccionById(id: Int) = transaccionDao.deleteTransaccionById(id)
    override fun getTransactionsFromLastThreeMonths(): Flow<List<Transaccion>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        return transaccionDao.getTransactionsFromLastThreeMonths(calendar.timeInMillis)
    }

    override fun getAllCategorias(): Flow<List<Categoria>> = categoriaDao.getAllCategorias()
    override suspend fun insertCategoria(categoria: Categoria) = categoriaDao.insertCategoria(categoria)
    override suspend fun deleteCategoria(categoria: Categoria) = categoriaDao.deleteCategoria(categoria)

    override fun getUsuario(): Flow<Usuario?> = usuarioDao.getUsuario()
    override suspend fun upsertUsuario(usuario: Usuario) = usuarioDao.upsertUsuario(usuario)

    override fun getAllMonedas(): Flow<List<Moneda>> = monedaDao.getAllMonedas()
    override suspend fun insertMoneda(moneda: Moneda) = monedaDao.insertMoneda(moneda)
    override suspend fun updateMoneda(moneda: Moneda) = monedaDao.updateMoneda(moneda)
}