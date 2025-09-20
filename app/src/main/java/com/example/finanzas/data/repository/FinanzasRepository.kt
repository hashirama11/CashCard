package com.example.finanzas.data.repository

import com.example.finanzas.data.local.entity.Budget
import com.example.finanzas.data.local.entity.BudgetCategory
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.model.BudgetCategoryDetail
import kotlinx.coroutines.flow.Flow

interface FinanzasRepository {
    // Funciones de Transacciones
    fun getAllTransacciones(): Flow<List<Transaccion>>
    suspend fun insertTransaccion(transaccion: Transaccion)
    suspend fun updateTransaction(transaccion: Transaccion)
    fun getTransaccionById(id: Int): Flow<Transaccion?>
    suspend fun deleteTransaccionById(id: Int)
    fun getTransactionsFromLastThreeMonths(): Flow<List<Transaccion>>


    // Funciones de Categorías
    fun getAllCategorias(): Flow<List<Categoria>>
    suspend fun insertCategoria(categoria: Categoria)
    suspend fun deleteCategoria(categoria: Categoria)

    // Funciones de Usuario
    fun getUsuario(): Flow<Usuario?>
    suspend fun upsertUsuario(usuario: Usuario)
    suspend fun getUsuarioSinc(): Usuario?

    // Funciones de Moneda
    fun getAllMonedas(): Flow<List<Moneda>>
    suspend fun insertMoneda(moneda: Moneda)
    suspend fun updateMoneda(moneda: Moneda)

    // Funciones de Cierre de Mes
    suspend fun realizarCorteDeMes()

    // Budget functions
    fun getBudgetDetails(month: Int, year: Int): Flow<List<BudgetCategoryDetail>>
    suspend fun saveBudget(budget: Budget, categories: List<BudgetCategory>)
    suspend fun getBudgetByMonthAndYear(month: Int, year: Int): Budget?
    suspend fun insertBudget(budget: Budget): Long
    suspend fun upsertBudgetCategory(budgetCategory: BudgetCategory)
    suspend fun getBudgetCategory(budgetId: Long, categoryId: Int): BudgetCategory?

    // Functions for budget creation
    suspend fun getCategoriasGastos(): List<Categoria>
    suspend fun getIncomeCategory(): Categoria?
    suspend fun getCategoriaById(id: Int): Categoria?
}