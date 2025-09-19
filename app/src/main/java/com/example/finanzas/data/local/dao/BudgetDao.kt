package com.example.finanzas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.finanzas.data.local.entity.Budget
import com.example.finanzas.data.local.entity.BudgetCategory
import com.example.finanzas.data.local.entity.BudgetWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetCategories(categories: List<BudgetCategory>)

    @Transaction
    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    fun getBudgetForMonth(month: Int, year: Int): Flow<BudgetWithCategories?>

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    suspend fun getBudgetByMonthAndYear(month: Int, year: Int): Budget?

    @Transaction
    suspend fun saveBudget(budget: Budget, categories: List<BudgetCategory>) {
        val budgetId = insertBudget(budget)
        val categoriesWithId = categories.map { it.copy(budgetId = budgetId.toInt()) }
        insertBudgetCategories(categoriesWithId)
    }
}
