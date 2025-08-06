package com.example.finanzas.repository

import com.example.finanzas.model.GastoDao
import com.example.finanzas.model.GastoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GastoRepositoryImpl @Inject constructor(
    private val gastoDao: GastoDao
) : GastoRepository {

    override fun obtenerGastos(): Flow<List<GastoEntity>> {
        return gastoDao.obtenerGastos()
    }

    override fun obtenerGastosPorCategoria(categoria: String): Flow<List<GastoEntity>> {
        return gastoDao.obtenerGastosPorCategoria(categoria)
    }

    override suspend fun crearGasto(gasto: GastoEntity) {
        gastoDao.crearGasto(gasto)
    }
    override fun obtenerTotalGastos(): Flow<Double?> {
        return gastoDao.obtenerTotalGastos()
    }


}