package com.example.finanzas.ui.monthly_goal

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyGoalScreen(
    viewModel: MonthlyGoalViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val usdFormat = NumberFormat.getCurrencyInstance(Locale.US).apply { currency = Currency.getInstance("USD") }
    val vesFormat = NumberFormat.getCurrencyInstance(Locale("es", "VE")).apply { currency = Currency.getInstance("VES") }

    var selectedCurrencyForChart by remember { mutableStateOf("USD") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rendimiento del Mes", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedCurrencyForChart == "USD",
                        onClick = { selectedCurrencyForChart = "USD" },
                        label = { Text("Ahorro en USD") }
                    )
                    FilterChip(
                        selected = selectedCurrencyForChart == "VES",
                        onClick = { selectedCurrencyForChart = "VES" },
                        label = { Text("Ahorro en VES") }
                    )
                }
            }

            item {
                SavingsProgressIndicator(
                    progress = if (selectedCurrencyForChart == "USD") state.savingsRateUsd else state.savingsRateVes,
                    balanceDelMes = if (selectedCurrencyForChart == "USD") state.balanceDelMesUsd else state.balanceDelMesVes,
                    currencyFormat = if (selectedCurrencyForChart == "USD") usdFormat else vesFormat,
                    currencyName = selectedCurrencyForChart
                )
            }

            item {
                MonthlyBreakdownCard(
                    title = "Movimientos del Mes",
                    ingresosUsd = state.ingresosDelMesUsd,
                    ingresosVes = state.ingresosDelMesVes,
                    gastosUsd = state.gastosDelMesUsd,
                    gastosVes = state.gastosDelMesVes,
                    usdFormat = usdFormat,
                    vesFormat = vesFormat
                )
            }

            item {
                InitialBalanceCard(
                    amount = state.ahorroAcumulado,
                    format = usdFormat
                )
            }
        }
    }
}

@Composable
fun SavingsProgressIndicator(
    progress: Float,
    balanceDelMes: Double,
    currencyFormat: NumberFormat,
    currencyName: String
) {
    val indicatorColor = if (balanceDelMes >= 0) AccentGreen else AccentRed

    var targetRate by remember { mutableFloatStateOf(0f) }
    val animatedRate by animateFloatAsState(
        targetValue = targetRate,
        animationSpec = tween(durationMillis = 1200), label = ""
    )
    LaunchedEffect(progress) {
        targetRate = progress
    }

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
                    color = Color.LightGray,
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
                Text(text = "Ahorro en $currencyName", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(
            text = "Balance del Mes: ${currencyFormat.format(balanceDelMes)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = indicatorColor
        )
    }
}

@Composable
fun MonthlyBreakdownCard(
    title: String,
    ingresosUsd: Double,
    ingresosVes: Double,
    gastosUsd: Double,
    gastosVes: Double,
    usdFormat: NumberFormat,
    vesFormat: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))
            BreakdownRow("Ingresos", vesFormat.format(ingresosVes), usdFormat.format(ingresosUsd), AccentGreen)
            BreakdownRow("Gastos", vesFormat.format(gastosVes), usdFormat.format(gastosUsd), AccentRed)
        }
    }
}

@Composable
fun BreakdownRow(label: String, amountVes: String, amountUsd: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = color, modifier = Modifier.weight(1f))
        Text(text = amountVes, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = color, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        Text(text = amountUsd, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = color, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
    Divider()
}

@Composable
fun InitialBalanceCard(amount: Double, format: NumberFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                text = format.format(amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End
            )
        }
    }
}