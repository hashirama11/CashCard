package com.example.finanzas.data.local.dao

import androidx.room.*
import com.example.finanzas.data.local.entity.Transaccion
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaccion(transaccion: Transaccion)

    @Update
    suspend fun updateTransaccion(transaccion: Transaccion)

    @Delete
    suspend fun deleteTransaccion(transaccion: Transaccion)

    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun getAllTransacciones(): Flow<List<Transaccion>>

    @Query("SELECT * FROM transacciones WHERE id = :id")
    fun getTransaccionById(id: Int): Flow<Transaccion?>

    @Query("DELETE FROM transacciones WHERE id = :id")
    suspend fun deleteTransaccionById(id: Int)

    @Query("SELECT * FROM transacciones WHERE fecha >= :threeMonthsAgo")
    fun getTransactionsFromLastThreeMonths(threeMonthsAgo: Long): Flow<List<Transaccion>>

    @Query("""
        SELECT COALESCE(SUM(monto), 0.0)
        FROM transacciones
        WHERE categoriaId = :categoryId
        AND tipo = 'GASTO'
        AND fecha BETWEEN :startDate AND :endDate
    """)
    fun getSumOfExpensesForCategory(categoryId: Int, startDate: Long, endDate: Long): Flow<Double>
}