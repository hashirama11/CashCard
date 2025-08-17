package com.example.finanzas.repository.gasto

import com.example.finanzas.model.gasto.GastoDao
import com.example.finanzas.model.gasto.GastoEntity
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

    override suspend fun eliminarTodosLosGastos() {
        gastoDao.eliminarTodosLosGastos()
    }

    override fun obtenerTotalGastosPorCategoria(categoria: String): Flow<Double?> {
        return gastoDao.obtenerTotalGastosPorCategoria(categoria)
    }

    override fun obtenerGastosPorCumplimiento(cumplimiento: Boolean): Flow<List<GastoEntity>> {
        return gastoDao.obtenerGastosPorCumplimiento(cumplimiento)
    }
    override fun obtenerGastosPorCumplimientoFalse(cumplimiento: Boolean): Flow<List<GastoEntity>> {
        return gastoDao.obtenerGastosPorCumplimientoFalse(cumplimiento)
    }


}