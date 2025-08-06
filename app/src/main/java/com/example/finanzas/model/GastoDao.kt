package com.example.finanzas.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GastoDao {

    // Obtener todos los gastos
    @Query("SELECT * FROM gastos")
    fun obtenerGastos(): List<GastoEntity>

    // Obtener los gastos por categoria
    @Query("SELECT * FROM gastos WHERE categoria = :categoria")
    fun obtenerGastosPorCategoria(categoria: String): List<GastoEntity>

    // Crear un nuevo gasto
    @Insert
    fun crearGasto(gasto: GastoEntity)

}