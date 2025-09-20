package com.example.finanzas.ui.budget.edit

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.ui.util.getIconResource
import java.text.NumberFormat
import java.util.Locale
import java.util.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBudgetScreen(
    viewModel: EditBudgetViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToCategoryManagement: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showRemoveCategoryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear/Editar Presupuesto") },
                navigationIcon = {
                    if (state.currentStep == 2) {
                        IconButton(onClick = { viewModel.onPreviousStep() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Crossfade(
            targetState = state.currentStep,
            modifier = Modifier.padding(paddingValues),
            label = "EditBudgetStep"
        ) { step ->
            when (step) {
                1 -> Step1_DefineIncome(
                    state = state,
                    onIncomeChanged = viewModel::onIncomeChanged,
                    onIncomeSecondaryChanged = viewModel::onIncomeSecondaryChanged,
                    onNext = viewModel::onNextStep
                )
                2 -> Step2_AssignLimits(
                    state = state,
                    onAmountChanged = viewModel::onBudgetAmountChanged,
                    onSave = viewModel::saveBudget,
                    onAddCategory = { showAddCategoryDialog = true },
                    onRemoveCategory = { showRemoveCategoryDialog = true }
                )
            }
        }
        if (showAddCategoryDialog) {
            AddCategoryToBudgetDialog(
                allCategories = state.allExpenseCategories,
                categoriesInBudget = state.expenseCategories,
                onAddCategory = viewModel::addCategoryToBudget,
                onDismiss = { showAddCategoryDialog = false }
            )
        }

        if (showRemoveCategoryDialog) {
            RemoveCategoryFromBudgetDialog(
                categoriesInBudget = state.expenseCategories,
                onRemoveCategory = viewModel::removeCategoryFromBudget,
                onDismiss = { showRemoveCategoryDialog = false }
            )
        }
    }
}

@Composable
fun Step1_DefineIncome(
    state: EditBudgetState,
    onIncomeChanged: (String) -> Unit,
    onIncomeSecondaryChanged: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Primero, ¿cuál es tu ingreso total proyectado para este mes?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = state.projectedIncome,
            onValueChange = onIncomeChanged,
            label = { Text("Ingreso en ${state.usuario?.monedaPrincipal ?: ""}") },
            prefix = { Text(state.usuario?.monedaPrincipal ?: "") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(Modifier.height(16.dp))
        if (!state.usuario?.monedaSecundaria.isNullOrEmpty()) {
            OutlinedTextField(
                value = state.projectedIncomeSecondary,
                onValueChange = onIncomeSecondaryChanged,
                label = { Text("Ingreso en ${state.usuario?.monedaSecundaria ?: ""}") },
                prefix = { Text(state.usuario?.monedaSecundaria ?: "") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
        Spacer(Modifier.height(32.dp))
        val totalIncome = (state.projectedIncome.toDoubleOrNull() ?: 0.0) + (state.projectedIncomeSecondary.toDoubleOrNull() ?: 0.0)
        Button(onClick = onNext, enabled = totalIncome > 0) {
            Text("Siguiente")
        }
    }
}

fun findSymbol(currencyCode: String): String {
    return Currency.getInstance(currencyCode).symbol
}

@Composable
fun Step2_AssignLimits(
    state: EditBudgetState,
    onAmountChanged: (Int, String) -> Unit,
    onSave: () -> Unit,
    onAddCategory: () -> Unit,
    onRemoveCategory: () -> Unit
) {
    val numberFormat = remember { NumberFormat.getCurrencyInstance(Locale.getDefault()) }
    val projectedIncome = (state.projectedIncome.toDoubleOrNull() ?: 0.0) + (state.projectedIncomeSecondary.toDoubleOrNull() ?: 0.0)
    val totalBudgeted = state.budgetedAmounts.values.sumOf { it.toDoubleOrNull() ?: 0.0 }
    val remaining = projectedIncome - totalBudgeted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Define tus límites de gasto en ${state.usuario?.monedaPrincipal ?: ""}", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                SummaryRow("Ingreso Proyectado:", numberFormat.format(projectedIncome))
                SummaryRow("Total Presupuestado:", numberFormat.format(totalBudgeted))
                SummaryRow("Restante:", numberFormat.format(remaining))
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.expenseCategories) { category ->
                CategoryBudgetItem(
                    category = category,
                    amount = state.budgetedAmounts[category.id] ?: "",
                    onAmountChanged = { onAmountChanged(category.id, it) },
                    currency = state.usuario?.monedaPrincipal ?: ""
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onAddCategory,
            ) {
                Text("Añadir Categoría")
            }
            Button(
                onClick = onRemoveCategory,
            ) {
                Text("Eliminar Categoría")
            }
            Button(
                onClick = onSave,
                enabled = totalBudgeted > 0,
            ) {
                Text("Guardar Presupuesto")
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}

@Composable
fun CategoryBudgetItem(
    category: Categoria,
    amount: String,
    onAmountChanged: (String) -> Unit,
    currency: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painterResource(id = getIconResource(category.icono)), contentDescription = category.nombre)
            Spacer(Modifier.width(16.dp))
            Text(category.nombre, modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = amount,
                onValueChange = onAmountChanged,
                label = { Text("Límite") },
                prefix = { Text(currency) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(120.dp)
            )
        }
    }
}
