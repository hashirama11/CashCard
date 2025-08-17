package com.example.finanzas.model.gasto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    // Obtener todos los gastos
    @Query("SELECT * FROM gastos")
    fun obtenerGastos(): Flow<List<GastoEntity>>

    // Obtener gastos por categoría
    @Query("SELECT * FROM gastos WHERE categoria = :categoria")
    fun obtenerGastosPorCategoria(categoria: String): Flow<List<GastoEntity>>

    // Crear un nuevo gasto
    @Insert
    suspend fun crearGasto(gasto: GastoEntity)

    // Sumar todos los montos
    @Query("SELECT SUM(monto) FROM gastos")
    fun obtenerTotalGastos(): Flow<Double?>

    // Sumar todos los montos por categoría
    @Query("SELECT SUM(monto) FROM gastos WHERE categoria = :categoria")
    fun obtenerTotalGastosPorCategoria(categoria: String): Flow<Double?>

    // Eliminar todos los gastos de la tabla
    @Query("DELETE FROM gastos")
    suspend fun eliminarTodosLosGastos()

    // Obtener todos los gasto donde el cumplimiento sea true
    @Query("SELECT * FROM gastos WHERE cumplimiento = :cumplimiento")
    fun obtenerGastosPorCumplimiento(cumplimiento: Boolean): Flow<List<GastoEntity>>

    // Obtener todos los gasto donde el cumplimiento sea false
    @Query("SELECT * FROM gastos WHERE cumplimiento = :cumplimiento")
    fun obtenerGastosPorCumplimientoFalse(cumplimiento: Boolean): Flow<List<GastoEntity>>


}