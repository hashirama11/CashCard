package com.example.finanzas.domain.use_case

import com.example.finanzas.model.BudgetCategoryDetail
import javax.inject.Inject

class ExportBudgetUseCase @Inject constructor() {

    operator fun invoke(
        incomeCategories: List<BudgetCategoryDetail>,
        expenseCategories: List<BudgetCategoryDetail>
    ): String {
        val csvBuilder = StringBuilder()

        // CSV Header
        csvBuilder.append("Tipo,Categoría,Presupuesto,Real,Porcentaje\n")

        // Income Rows
        incomeCategories.forEach { detail ->
            val percentage = if (detail.budgetedAmount > 0) (detail.actualAmount / detail.budgetedAmount * 100) else 0.0
            csvBuilder.append(
                "${detail.categoryType},${detail.categoryName},${detail.budgetedAmount},${detail.actualAmount},${"%.2f".format(percentage)}%\n"
            )
        }

        // Add a blank line for separation
        csvBuilder.append("\n")

        // Expense Rows
        expenseCategories.forEach { detail ->
            val percentage = if (detail.budgetedAmount > 0) (detail.actualAmount / detail.budgetedAmount * 100) else 0.0
            csvBuilder.append(
                "${detail.categoryType},${detail.categoryName},${detail.budgetedAmount},${detail.actualAmount},${"%.2f".format(percentage)}%\n"
            )
        }

        return csvBuilder.toString()
    }
}
