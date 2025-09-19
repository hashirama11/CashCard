package com.example.finanzas.domain.use_case

import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.BudgetCategoryDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMonthlyBudgetDetailsUseCase @Inject constructor(
    private val finanzasRepository: FinanzasRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<List<BudgetCategoryDetail>> {
        return finanzasRepository.getBudgetDetails(month, year)
    }
}
