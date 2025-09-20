package com.example.finanzas.ui.budget.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.domain.use_case.CreateMonthlyBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class EditBudgetState(
    val isLoading: Boolean = true,
    val projectedIncome: String = "",
    val expenseCategories: List<Categoria> = emptyList(),
    val budgetedAmounts: Map<Int, String> = emptyMap(),
    val isSaved: Boolean = false,
    val currentStep: Int = 1,
    val selectedDate: Calendar = Calendar.getInstance()
)

@HiltViewModel
class EditBudgetViewModel @Inject constructor(
    private val finanzasRepository: FinanzasRepository,
    private val createMonthlyBudgetUseCase: CreateMonthlyBudgetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditBudgetState())
    val uiState get() = _uiState.asStateFlow()

    init {
        loadExpenseCategories()
    }

    private fun loadExpenseCategories() {
        viewModelScope.launch {
            val categories = finanzasRepository.getCategoriasGastos()
            _uiState.update { it.copy(isLoading = false, expenseCategories = categories) }
        }
    }

    fun onIncomeChanged(income: String) {
        _uiState.update { it.copy(projectedIncome = income) }
    }

    fun onBudgetAmountChanged(categoryId: Int, amount: String) {
        _uiState.update {
            val newAmounts = it.budgetedAmounts.toMutableMap()
            newAmounts[categoryId] = amount
            it.copy(budgetedAmounts = newAmounts)
        }
    }

    fun onNextStep() {
        _uiState.update { it.copy(currentStep = 2) }
    }

    fun onPreviousStep() {
        _uiState.update { it.copy(currentStep = 1) }
    }

    fun saveBudget() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val projectedIncome = currentState.projectedIncome.toDoubleOrNull() ?: 0.0

            // Create the map for the use case
            val categoriesMap = mutableMapOf<Int, Double>()

            // Add expense categories
            currentState.budgetedAmounts.forEach { (id, amountStr) ->
                val amount = amountStr.toDoubleOrNull()
                if (amount != null && amount > 0) {
                    categoriesMap[id] = amount
                }
            }

            // Add income category
            val incomeCategory = finanzasRepository.getIncomeCategory()
            if (incomeCategory != null && projectedIncome > 0) {
                categoriesMap[incomeCategory.id] = projectedIncome
            }

            if (categoriesMap.isNotEmpty()) {
                createMonthlyBudgetUseCase(
                    year = currentState.selectedDate.get(Calendar.YEAR),
                    month = currentState.selectedDate.get(Calendar.MONTH),
                    categories = categoriesMap
                )
                _uiState.update { it.copy(isSaved = true) }
            }
        }
    }
}
