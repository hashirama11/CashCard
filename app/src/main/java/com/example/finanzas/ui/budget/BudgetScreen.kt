package com.example.finanzas.ui.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.BudgetCategoryDetail
import com.example.finanzas.ui.util.getIconResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(viewModel: BudgetViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Presupuesto Mensual") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                uiState.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
                BudgetSummarySection(summary = uiState.overallSummary)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.budgetedCategories) { categoryDetail ->
                        BudgetCategoryItem(detail = categoryDetail)
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetSummarySection(summary: BudgetSummary?) {
    if (summary == null) return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resumen del Mes", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ingreso Proyectado:")
                Text("${summary.projectedIncome}", fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gasto Presupuestado:")
                Text("${summary.budgetedSpending}", fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gasto Real:")
                Text("${summary.actualSpending}", fontWeight = FontWeight.Bold, color = if(summary.actualSpending > summary.budgetedSpending) MaterialTheme.colorScheme.error else Color.Unspecified)
            }
        }
    }
}

@Composable
fun BudgetCategoryItem(detail: BudgetCategoryDetail) {
    val spendingRatio = if (detail.budgetedAmount > 0) detail.actualSpending / detail.budgetedAmount else 0.0
    val isOverBudget = spendingRatio > 1.0
    val progressColor = if (isOverBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val remainingAmount = detail.budgetedAmount - detail.actualSpending
    val remainingText = if (remainingAmount >= 0) "Te quedan $${"%.2f".format(remainingAmount)}" else "Te has pasado por $${"%.2f".format(-remainingAmount)}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = getIconResource(detail.icon)),
                    contentDescription = detail.categoryName,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(detail.categoryName, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = spendingRatio.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                color = progressColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("$${"%.2f".format(detail.actualSpending)} / $${"%.2f".format(detail.budgetedAmount)}", fontSize = 14.sp)
                Text(remainingText, fontSize = 14.sp, color = if (isOverBudget) MaterialTheme.colorScheme.error else Color.Gray)
            }
        }
    }
}
