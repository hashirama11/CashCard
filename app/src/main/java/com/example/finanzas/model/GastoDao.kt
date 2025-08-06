package com.example.finanzas.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    // Obtener todos los gastos
    @Query("SELECT * FROM gastos")
    fun obtenerGastos(): Flow<List<GastoEntity>>

    @Query("SELECT * FROM gastos WHERE categoria = :categoria")
    fun obtenerGastosPorCategoria(categoria: String): Flow<List<GastoEntity>>

    // Crear un nuevo gasto
    @Insert
    suspend fun crearGasto(gasto: GastoEntity)

    // Sumar todos los montos
    @Query("SELECT SUM(monto) FROM gastos")
    fun obtenerTotalGastos(): Flow<Double?>

}