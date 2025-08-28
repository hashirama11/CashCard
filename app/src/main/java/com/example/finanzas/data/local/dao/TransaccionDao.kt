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
}