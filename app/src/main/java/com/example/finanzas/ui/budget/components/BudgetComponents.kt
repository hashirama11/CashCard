package com.example.finanzas.ui.budget.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.model.BudgetCategoryDetail
import com.example.finanzas.ui.budget.BudgetSummary
import com.example.finanzas.ui.util.getIconResource
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MonthSelector(
    selectedDate: Calendar,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
        }
        Text(
            text = dateFormat.format(selectedDate.time).replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }
}

@Composable
fun SummaryCard(
    summary: BudgetSummary,
    numberFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resumen del Mes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SummaryItem(
                    label = "Ingresos",
                    actual = summary.actualIncome,
                    projected = summary.projectedIncome,
                    numberFormat = numberFormat,
                    modifier = Modifier.weight(1f)
                )
                BalanceItem(
                    balance = summary.balance,
                    numberFormat = numberFormat,
                    modifier = Modifier.weight(0.8f)
                )
                SummaryItem(
                    label = "Gastos",
                    actual = summary.actualExpenses,
                    projected = summary.budgetedExpenses,
                    numberFormat = numberFormat,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    actual: Double,
    projected: Double,
    numberFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    val progress = if (projected > 0) (actual / projected).toFloat() else 0f
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(
            "${numberFormat.format(actual)} / ${numberFormat.format(projected)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(0.8f).height(8.dp).clip(CircleShape)
        )
    }
}

@Composable
private fun BalanceItem(
    balance: Double,
    numberFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    val balanceColor = if (balance >= 0) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = numberFormat.format(balance),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = balanceColor
        )
        Text("Balance Actual", style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun BudgetCategoryItem(
    detail: BudgetCategoryDetail,
    numberFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    val progress = if (detail.budgetedAmount > 0) (detail.actualAmount / detail.budgetedAmount).toFloat() else 0f
    val isOverBudget = progress > 1.0f
    val isWarning = progress in 0.9f..1.0f

    val progressColor = when {
        isOverBudget -> MaterialTheme.colorScheme.error
        isWarning -> Color(0xFFFFA726) // Amber
        else -> MaterialTheme.colorScheme.primary
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = getIconResource(detail.icon)),
                    contentDescription = detail.categoryName,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = detail.categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${numberFormat.format(detail.actualAmount)} / ${numberFormat.format(detail.budgetedAmount)}",
                        fontWeight = FontWeight.SemiBold,
                        color = if (isOverBudget) MaterialTheme.colorScheme.error else Color.Unspecified
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = progressColor,
                trackColor = progressColor.copy(alpha = 0.3f)
            )
            Spacer(Modifier.height(8.dp))
            StatusText(
                detail = detail,
                isOverBudget = isOverBudget,
                isWarning = isWarning,
                numberFormat = numberFormat,
                progressColor = progressColor
            )
        }
    }
}

@Composable
private fun StatusText(
    detail: BudgetCategoryDetail,
    isOverBudget: Boolean,
    isWarning: Boolean,
    numberFormat: NumberFormat,
    progressColor: Color
) {
    val statusIcon = when {
        isOverBudget -> Icons.Filled.Error
        isWarning -> Icons.Filled.Warning
        else -> Icons.Filled.CheckCircle
    }
    val statusText = when {
        isOverBudget -> "Te has excedido por ${numberFormat.format(detail.actualAmount - detail.budgetedAmount)}"
        isWarning -> "Límite casi alcanzado"
        else -> "Aún te quedan ${numberFormat.format(detail.budgetedAmount - detail.actualAmount)}"
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = statusIcon,
            contentDescription = "Status",
            tint = progressColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodySmall,
            color = progressColor
        )
    }
}


@Composable
fun EmptyState(
    selectedDate: Calendar,
    onCreateBudget: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                painter = painterResource(id = getIconResource("INTERESES_BANCARIOS")),
                contentDescription = "Empty",
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Aún no tienes un presupuesto para ${dateFormat.format(selectedDate.time)}.",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "¡Toma el control de tus finanzas! Crear un presupuesto te ayuda a planificar tus gastos y alcanzar tus metas.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onCreateBudget,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Crear Presupuesto")
            }
        }
    }
}
