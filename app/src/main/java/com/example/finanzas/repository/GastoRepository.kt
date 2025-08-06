package com.example.finanzas.repository

import com.example.finanzas.model.GastoEntity
import kotlinx.coroutines.flow.Flow

interface GastoRepository {

    fun obtenerGastos(): Flow<List<GastoEntity>>
    fun obtenerGastosPorCategoria(categoria: String): Flow<List<GastoEntity>>
    suspend fun crearGasto(gasto: GastoEntity)
    fun obtenerTotalGastos(): Flow<Double?>

}