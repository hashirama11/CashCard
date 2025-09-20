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

import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDashboardScreen(
    viewModel: BudgetDashboardViewModel = hiltViewModel(),
    onNavigateToCreateBudget: () -> Unit,
    onNavigateToCategoryManagement: () -> Unit,
    onCategoryClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val numberFormat = remember(state.locale) {
        (NumberFormat.getCurrencyInstance(state.locale) as DecimalFormat).apply {
            maximumFractionDigits = 2
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    MonthSelector(
                        selectedDate = state.selectedDate,
                        onPreviousMonth = { viewModel.onPreviousMonth() },
                        onNextMonth = { viewModel.onNextMonth() }
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToCategoryManagement) {
                        Icon(Icons.Default.Add, contentDescription = "Manage Categories")
                    }
                }
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
                    modifier = Modifier.padding(paddingValues),
                    onCategoryClick = onCategoryClick
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

import androidx.compose.foundation.layout.Column

@Composable
fun BudgetDetailContent(
    state: BudgetDashboardState,
    numberFormat: NumberFormat,
    viewModel: BudgetDashboardViewModel,
    modifier: Modifier = Modifier,
    onCategoryClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SummaryCard(
                    summary = state.budgetSummary,
                    balance = state.balance,
                    numberFormat = numberFormat
                )
            }
            item {
                Text(
                    text = "Detalle de Gastos",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(state.expenseCategories) { detail ->
            BudgetCategoryItem(
                detail = detail,
                numberFormat = numberFormat,
                onClick = { onCategoryClick(detail.categoryId) }
            )
        }
    }
}
