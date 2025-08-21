package com.example.finanzas.ui.balance.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SavingsRateIndicator(
    netBalanceVes: Double,
    netBalanceUsd: Double,
    savingsRate: Float,
) {
    val vesFormat = NumberFormat.getCurrencyInstance(Locale("es", "VE")).apply {
        currency = Currency.getInstance("VES")
        maximumFractionDigits = 2
    }
    val usdFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        currency = Currency.getInstance("USD")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                var targetRate by remember { mutableStateOf(0f) }
                val animatedRate by animateFloatAsState(
                    targetValue = targetRate,
                    animationSpec = tween(durationMillis = 1000), label = ""
                )
                LaunchedEffect(savingsRate) {
                    targetRate = savingsRate
                }

                val indicatorColor = if (netBalanceVes + netBalanceUsd >= 0) AccentGreen else AccentRed

                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 25f, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = indicatorColor,
                        startAngle = -90f,
                        sweepAngle = 360 * animatedRate,
                        useCenter = false,
                        style = Stroke(width = 25f, cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "${(animatedRate * 100).roundToInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "Balance Neto",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = usdFormat.format(netBalanceUsd),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (netBalanceUsd >= 0) AccentGreen else AccentRed
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = vesFormat.format(netBalanceVes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (netBalanceVes >= 0) AccentGreen else AccentRed
                )
            }
        }
    }
}