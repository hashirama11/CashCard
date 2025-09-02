package com.example.finanzas.ui.monthly_goal

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import com.example.finanzas.ui.components.LoadingIndicator
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyGoalScreen(
    viewModel: MonthlyGoalViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val numberFormat = remember(state.selectedCurrency) {
        state.selectedCurrency?.let { moneda ->
            (NumberFormat.getCurrencyInstance() as DecimalFormat).apply {
                maximumFractionDigits = 2
                val symbols = this.decimalFormatSymbols
                symbols.currencySymbol = moneda.simbolo
                this.decimalFormatSymbols = symbols
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meta Mensual", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        if (state.isLoading || numberFormat == null) {
            LoadingIndicator()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(state.usedCurrencies) { moneda ->
                            FilterChip(
                                selected = state.selectedCurrency == moneda,
                                onClick = { viewModel.onCurrencySelected(moneda) },
                                label = { Text(moneda.nombre) }
                            )
                        }
                    }
                }

                if (state.monthlyGoal > 0) {
                    item {
                        GoalProgressCard(
                            balanceDelMes = state.balanceDelMes,
                            monthlyGoal = state.monthlyGoal,
                            numberFormat = numberFormat
                        )
                    }
                }

                item {
                    SavingsRateIndicator(
                        progress = state.savingsRate,
                        balanceDelMes = state.balanceDelMes,
                        numberFormat = numberFormat,
                        currencyName = state.selectedCurrency?.nombre ?: ""
                    )
                }

                item {
                    MonthlyBreakdownCard(
                        title = "Movimientos del Mes",
                        ingresos = state.ingresosDelMes,
                        gastos = state.gastosDelMes,
                        numberFormat = numberFormat
                    )
                }

                if (state.ahorroAcumulado > 0) {
                    item {
                        InitialBalanceCard(
                            amount = state.ahorroAcumulado,
                            numberFormat = numberFormat
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GoalProgressCard(balanceDelMes: Double, monthlyGoal: Double, numberFormat: NumberFormat) {
    val progress = if (monthlyGoal > 0) (balanceDelMes / monthlyGoal).toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(1000), label = ""
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Progreso de Meta Mensual",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(MaterialTheme.shapes.small),
                trackColor = MaterialTheme.colorScheme.surface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${numberFormat.format(balanceDelMes)} de ${numberFormat.format(monthlyGoal)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun SavingsRateIndicator(
    progress: Float,
    balanceDelMes: Double,
    numberFormat: NumberFormat,
    currencyName: String
) {
    val indicatorColor = if (balanceDelMes >= 0) AccentGreen else AccentRed
    val animatedRate by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1200), label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 40f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = indicatorColor,
                    startAngle = -90f,
                    sweepAngle = 360 * animatedRate,
                    useCenter = false,
                    style = Stroke(width = 40f, cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.savings_24dp),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(animatedRate * 100).roundToInt()}%",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = "Tasa de Ahorro", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(
            text = "Balance del Mes: ${numberFormat.format(balanceDelMes)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = indicatorColor
        )
    }
}

@Composable
fun MonthlyBreakdownCard(
    title: String,
    ingresos: Double,
    gastos: Double,
    numberFormat: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))
            BreakdownRow("Ingresos", numberFormat.format(ingresos), AccentGreen)
            Divider()
            BreakdownRow("Gastos", numberFormat.format(gastos), AccentRed)
        }
    }
}

@Composable
fun BreakdownRow(label: String, amount: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = color)
        Text(text = amount, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = color)
    }
}

@Composable
fun InitialBalanceCard(amount: Double, numberFormat: NumberFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Saldo Inicial (Ahorro Anterior)",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = numberFormat.format(amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End
            )
        }
    }
}