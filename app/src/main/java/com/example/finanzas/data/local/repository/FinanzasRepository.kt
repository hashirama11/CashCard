package com.example.finanzas.data.repository

import com.example.finanzas.data.local.entity.Transaccion
import kotlinx.coroutines.flow.Flow

// Definimos qué puede hacer nuestro repositorio
interface FinanzasRepository {
    fun getAllTransacciones(): Flow<List<Transaccion>>
    suspend fun insertTransaccion(transaccion: Transaccion)
    // Agregaremos más funciones aquí para categorías y usuario a medida que las necesitemos
}