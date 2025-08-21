package com.example.finanzas.data.repository

import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

interface FinanzasRepository {
    // Funciones de Transacciones
    fun getAllTransacciones(): Flow<List<Transaccion>>
    suspend fun insertTransaccion(transaccion: Transaccion)
    suspend fun updateTransaction(transaccion: Transaccion)
    fun getTransaccionById(id: Int): Flow<Transaccion?>
    suspend fun deleteTransaccionById(id: Int)

    // Funciones de Categorías
    fun getAllCategorias(): Flow<List<Categoria>>
    suspend fun insertCategoria(categoria: Categoria)
    suspend fun deleteCategoria(categoria: Categoria)

    // Funciones de Usuario
    fun getUsuario(): Flow<Usuario?>
    suspend fun upsertUsuario(usuario: Usuario)
}