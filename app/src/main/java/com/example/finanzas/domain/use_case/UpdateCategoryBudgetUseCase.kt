package com.example.finanzas.domain.use_case

import com.example.finanzas.data.local.entity.Budget
import com.example.finanzas.data.local.entity.BudgetCategory
import com.example.finanzas.data.repository.FinanzasRepository
import javax.inject.Inject

class UpdateCategoryBudgetUseCase @Inject constructor(
    private val repository: FinanzasRepository
) {
    suspend operator fun invoke(year: Int, month: Int, categoryId: Int, amount: Double) {
        // First, check if a budget for the month already exists.
        var budget = repository.getBudgetByMonthAndYear(month, year)
        val budgetId: Long

        if (budget == null) {
            // If not, create a new one and get its ID
            budgetId = repository.insertBudget(Budget(year = year, month = month))
        } else {
            budgetId = budget.id.toLong()
        }

        // Now, check if a budget category entry already exists for this budget and category
        val existingBudgetCategory = repository.getBudgetCategory(budgetId, categoryId)

        val budgetCategoryToUpsert = if (existingBudgetCategory != null) {
            // If it exists, update the amount
            existingBudgetCategory.copy(budgetedAmount = amount)
        } else {
            // If not, create a new one
            BudgetCategory(
                budgetId = budgetId,
                categoryId = categoryId,
                budgetedAmount = amount
            )
        }

        // Upsert the budget category (insert or update)
        repository.upsertBudgetCategory(budgetCategoryToUpsert)
    }
}
