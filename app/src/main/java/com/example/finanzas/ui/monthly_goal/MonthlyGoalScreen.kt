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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R // Asegúrate de tener los íconos en tu carpeta res/drawable
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
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        currency = Currency.getInstance("USD")
    }
    // Calculamos el porcentaje de ahorro sobre los ingresos del mes
    val savingsRate = if (state.ingresosDelMes > 0) {
        (state.balanceDelMes / state.ingresosDelMes).toFloat()
    } else {
        0f
    }.coerceIn(0f, 1f) // Lo limitamos entre 0 y 1

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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Indicador de Progreso Circular ---
            item {
                SavingsProgressIndicator(
                    progress = savingsRate,
                    balanceDelMes = state.balanceDelMes,
                    format = currencyFormat
                )
            }

            // --- Tarjetas de Resumen ---
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SummaryCard(
                        title = "Ingresos del Mes",
                        amount = state.ingresosDelMes,
                        format = currencyFormat,
                        color = AccentGreen,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Gastos del Mes",
                        amount = state.gastosDelMes,
                        format = currencyFormat,
                        color = AccentRed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // --- Saldo Inicial ---
            item {
                InitialBalanceCard(
                    amount = state.ahorroAcumulado,
                    format = currencyFormat
                )
            }
        }
    }
}

@Composable
fun SavingsProgressIndicator(progress: Float, balanceDelMes: Double, format: NumberFormat) {
    val indicatorColor = if (balanceDelMes >= 0) AccentGreen else AccentRed
    val illustration = if (balanceDelMes >= 0) R.drawable.savings_24dp else R.drawable.credit_card_24dp

    var targetRate by remember { mutableStateOf(0f) }
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
                    painter = painterResource(id = illustration),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(animatedRate * 100).roundToInt()}%",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Ahorrado",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Text(
            text = "Balance del Mes: ${format.format(balanceDelMes)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = indicatorColor
        )
    }
}


@Composable
fun SummaryCard(title: String, amount: Double, format: NumberFormat, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = format.format(amount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
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