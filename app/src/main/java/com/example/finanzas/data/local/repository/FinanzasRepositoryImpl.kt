package com.example.finanzas.data.repository

import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.entity.Transaccion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Le decimos cómo hacer lo que la interfaz promete, usando los DAOs
class FinanzasRepositoryImpl @Inject constructor(
    private val transaccionDao: TransaccionDao
    // Inyectaremos CategoriaDao y UsuarioDao aquí cuando los necesitemos
) : FinanzasRepository {

    override fun getAllTransacciones(): Flow<List<Transaccion>> {
        return transaccionDao.getAllTransacciones()
    }

    override suspend fun insertTransaccion(transaccion: Transaccion) {
        transaccionDao.insertTransaccion(transaccion)
    }
}