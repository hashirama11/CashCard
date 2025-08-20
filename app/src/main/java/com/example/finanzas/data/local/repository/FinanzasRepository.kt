package com.example.finanzas.data.repository

import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

interface FinanzasRepository {
    // Funciones de Transacciones
    fun getAllTransacciones(): Flow<List<Transaccion>>
    suspend fun insertTransaccion(transaccion: Transaccion)

    // Funciones de Categorías
    fun getAllCategorias(): Flow<List<Categoria>>
    suspend fun insertCategoria(categoria: Categoria)

    // Funciones de Usuario
    fun getUsuario(): Flow<Usuario?>
    suspend fun upsertUsuario(usuario: Usuario)
}