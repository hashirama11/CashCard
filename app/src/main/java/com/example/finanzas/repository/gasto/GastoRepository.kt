package com.example.finanzas.repository.gasto

import com.example.finanzas.model.gasto.GastoEntity
import kotlinx.coroutines.flow.Flow

interface GastoRepository {

    fun obtenerGastos(): Flow<List<GastoEntity>>
    fun obtenerGastosPorCategoria(categoria: String): Flow<List<GastoEntity>>
    suspend fun crearGasto(gasto: GastoEntity)
    fun obtenerTotalGastos(): Flow<Double?>
    suspend fun eliminarTodosLosGastos()

    fun obtenerTotalGastosPorCategoria(categoria: String): Flow<Double?>

}