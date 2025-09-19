package com.example.finanzas.domain.use_case

import com.example.finanzas.data.local.entity.Budget
import com.example.finanzas.data.local.entity.BudgetCategory
import com.example.finanzas.data.repository.FinanzasRepository
import javax.inject.Inject

class CreateMonthlyBudgetUseCase @Inject constructor(
    private val finanzasRepository: FinanzasRepository
) {
    suspend operator fun invoke(
        year: Int,
        month: Int,
        categories: Map<Int, Double>
    ) {
        val budget = Budget(
            year = year,
            month = month
        )
        val budgetCategories = categories.map { (categoryId, amount) ->
            BudgetCategory(
                budgetId = 0L, // This will be replaced by the actual budget id
                categoryId = categoryId,
                budgetedAmount = amount
            )
        }
        finanzasRepository.saveBudget(budget, budgetCategories)
    }
}
