package com.example.finanzas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.finanzas.data.local.entity.Moneda
import kotlinx.coroutines.flow.Flow

@Dao
interface MonedaDao {
    @Query("SELECT * FROM monedas")
    fun getAllMonedas(): Flow<List<Moneda>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoneda(moneda: Moneda)

    @Update
    suspend fun updateMoneda(moneda: Moneda)
}
