package com.example.finanzas.ui.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.ui.budget.components.BudgetCategoryItem
import com.example.finanzas.ui.budget.components.EmptyState
import com.example.finanzas.ui.budget.components.MonthSelector
import com.example.finanzas.ui.budget.components.SummaryCard
import com.example.finanzas.ui.components.LoadingIndicator
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDashboardScreen(
    viewModel: BudgetDashboardViewModel = hiltViewModel(),
    onNavigateToCreateBudget: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val numberFormat = remember {
        (NumberFormat.getCurrencyInstance(Locale.getDefault()) as DecimalFormat).apply {
            maximumFractionDigits = 2
            // In a real app, this would come from user settings
            val symbols = this.decimalFormatSymbols
            symbols.currencySymbol = "$" // Using $ as a placeholder
            this.decimalFormatSymbols = symbols
        }
    }

    Scaffold(
        topBar = {
            MonthSelector(
                selectedDate = state.selectedDate,
                onPreviousMonth = { viewModel.onPreviousMonth() },
                onNextMonth = { viewModel.onNextMonth() }
            )
        },
        floatingActionButton = {
            if (state.hasBudget) {
                FloatingActionButton(onClick = onNavigateToCreateBudget) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Budget")
                }
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingIndicator()
        } else {
            if (state.hasBudget) {
                BudgetDetailContent(
                    state = state,
                    numberFormat = numberFormat,
                    viewModel = viewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                EmptyState(
                    selectedDate = state.selectedDate,
                    onCreateBudget = onNavigateToCreateBudget,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun BudgetDetailContent(
    state: BudgetDashboardState,
    numberFormat: NumberFormat,
    viewModel: BudgetDashboardViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MonthSelector(
                selectedDate = state.selectedDate,
                onPreviousMonth = { viewModel.onPreviousMonth() },
                onNextMonth = { viewModel.onNextMonth() }
            )
        }
        item {
            SummaryCard(
                summary = state.budgetSummary,
                numberFormat = numberFormat
            )
        }
        item {
            Text("Detalle de Gastos", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        }
        items(state.expenseCategories) { detail ->
            BudgetCategoryItem(
                detail = detail,
                numberFormat = numberFormat
            )
        }
    }
}
