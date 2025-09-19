package com.example.finanzas.ui.budget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.BudgetCategoryDetail
import com.example.finanzas.ui.components.LoadingIndicator
import com.example.finanzas.ui.util.getIconResource
import androidx.compose.ui.res.painterResource
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDashboardScreen(
    viewModel: BudgetDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<BudgetCategoryDetail?>(null) }

    // TODO: The real implementation would use the Android Storage Access Framework.
    // This is a placeholder to demonstrate the CSV content generation.
    val context = LocalContext.current
    LaunchedEffect(state.csvContent) {
        state.csvContent?.let { content ->
            // In a real app, you would launch an Intent here to save the file.
            // For example, using ActivityResultLauncher with ACTION_CREATE_DOCUMENT.
            println("CSV Content to save:\n$content")
            // Make sure to call onExportHandled to reset the state and avoid re-triggering.
            viewModel.onExportHandled()
        }
    }

    val numberFormat = remember {
        (NumberFormat.getCurrencyInstance() as DecimalFormat).apply {
            maximumFractionDigits = 2
            // In a real app, this would come from user settings
            val symbols = this.decimalFormatSymbols
            symbols.currencySymbol = "€"
            this.decimalFormatSymbols = symbols
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Presupuesto y Metas", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingIndicator()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // --- Monthly Goal Section ---
                item {
                    MonthlyGoalSection(
                        summary = state.monthlyGoalSummary,
                        numberFormat = numberFormat
                    )
                }

                // --- Income Budget Section ---
                item {
                    Text("Ingresos Presupuestados", style = MaterialTheme.typography.headlineSmall)
                }
                items(state.incomeCategories) { detail ->
                    BudgetItem(
                        detail = detail,
                        numberFormat = numberFormat,
                        onEditClick = {
                            selectedCategory = it
                            showDialog = true
                        }
                    )
                }

                // --- Expense Budget Section ---
                item {
                    Text("Gastos Presupuestados", style = MaterialTheme.typography.headlineSmall)
                }
                items(state.expenseCategories) { detail ->
                    BudgetItem(
                        detail = detail,
                        numberFormat = numberFormat,
                        onEditClick = {
                            selectedCategory = it
                            showDialog = true
                        }
                    )
                }

                // --- Export Button ---
                item {
                    OutlinedButton(
                        onClick = { viewModel.onExportClicked() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Exportar a Hojas de cálculo")
                    }
                }
            }
        }
    }

    if (showDialog && selectedCategory != null) {
        EditBudgetDialog(
            categoryDetail = selectedCategory!!,
            onDismiss = { showDialog = false },
            onConfirm = { newAmount ->
                viewModel.updateBudgetForCategory(selectedCategory!!.categoryId, newAmount)
                showDialog = false
            },
            numberFormat = numberFormat
        )
    }
}

@Composable
fun BudgetItem(
    detail: BudgetCategoryDetail,
    numberFormat: NumberFormat,
    onEditClick: (BudgetCategoryDetail) -> Unit
) {
    val progress = (detail.actualAmount / detail.budgetedAmount).toFloat().coerceIn(0f, 2f) // Allow over 100%
    val isOverBudget = progress > 1.0f

    val progressColor = when {
        isOverBudget -> MaterialTheme.colorScheme.error
        progress > 0.9f -> Color(0xFFFFA726) // Orange
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onEditClick(detail) },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = getIconResource(detail.icon)),
                    contentDescription = detail.categoryName,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(detail.categoryName, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Text(
                    text = "${(progress * 100).roundToInt()}%",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = progressColor
                )
            }
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(MaterialTheme.shapes.small),
                color = progressColor,
                trackColor = progressColor.copy(alpha = 0.3f)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${numberFormat.format(detail.actualAmount)} / ${numberFormat.format(detail.budgetedAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if(isOverBudget) {
                    Text(
                        text = "Excedido por ${numberFormat.format(detail.actualAmount - detail.budgetedAmount)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyGoalSection(summary: MonthlyGoalSummary, numberFormat: NumberFormat) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (summary.monthlyGoal > 0) {
            GoalProgressCard(
                balanceDelMes = summary.balanceDelMes,
                monthlyGoal = summary.monthlyGoal,
                numberFormat = numberFormat
            )
        }
        SavingsRateIndicator(
            progress = summary.savingsRate,
            balanceDelMes = summary.balanceDelMes,
            numberFormat = numberFormat
        )
    }
}

@Composable
fun EditBudgetDialog(
    categoryDetail: BudgetCategoryDetail,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit,
    numberFormat: NumberFormat
) {
    var textValue by remember { mutableStateOf(categoryDetail.budgetedAmount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Presupuesto para ${categoryDetail.categoryName}") },
        text = {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Monto Presupuestado") },
                prefix = { Text(numberFormat.currency?.symbol ?: "$") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = textValue.toDoubleOrNull()
                    if (amount != null) {
                        onConfirm(amount)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// --- Composables from old MonthlyGoalScreen (adapted) ---

@Composable
fun GoalProgressCard(balanceDelMes: Double, monthlyGoal: Double, numberFormat: NumberFormat) {
    val progress = if (monthlyGoal > 0) (balanceDelMes / monthlyGoal).toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(1000), label = ""
    )
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Progreso de Meta Mensual", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier.fillMaxWidth().height(12.dp).clip(MaterialTheme.shapes.small),
            )
            Spacer(Modifier.height(8.dp))
            Text(text = "${numberFormat.format(balanceDelMes)} de ${numberFormat.format(monthlyGoal)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SavingsRateIndicator(progress: Float, balanceDelMes: Double, numberFormat: NumberFormat) {
    val indicatorColor = if (balanceDelMes >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
    val animatedRate by animateFloatAsState(targetValue = progress, animationSpec = tween(durationMillis = 1200), label = "")
    Box(modifier = Modifier.size(180.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(color = Color.LightGray.copy(alpha = 0.3f), startAngle = -90f, sweepAngle = 360f, useCenter = false, style = Stroke(width = 40f, cap = StrokeCap.Round))
            drawArc(color = indicatorColor, startAngle = -90f, sweepAngle = 360 * animatedRate, useCenter = false, style = Stroke(width = 40f, cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${(animatedRate * 100).roundToInt()}%", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
            Text(text = "Tasa de Ahorro", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
