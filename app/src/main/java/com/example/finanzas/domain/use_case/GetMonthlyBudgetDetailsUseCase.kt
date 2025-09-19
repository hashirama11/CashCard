package com.example.finanzas.domain.use_case

import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.BudgetCategoryDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class BudgetDetails(
    val incomeCategories: List<BudgetCategoryDetail>,
    val expenseCategories: List<BudgetCategoryDetail>
)

class GetMonthlyBudgetDetailsUseCase @Inject constructor(
    private val finanzasRepository: FinanzasRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<BudgetDetails> {
        return finanzasRepository.getBudgetDetails(month, year).map { details ->
            val (income, expenses) = details.partition { it.categoryType == "INGRESO" }
            BudgetDetails(incomeCategories = income, expenseCategories = expenses)
        }
    }
}
