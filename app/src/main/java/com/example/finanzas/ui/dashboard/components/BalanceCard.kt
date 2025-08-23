package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Composable
fun BalanceCard(
    balanceVes: Double,
    balanceUsd: Double,
    ahorroAcumulado: Double,
    type: TipoTransaccion // 'type' nos dice si estamos en la pestaña Ingresos o Gastos
) {
    val vesFormat = NumberFormat.getCurrencyInstance(Locale("es", "VE")).apply {
        currency = Currency.getInstance("VES")
        maximumFractionDigits = 2
    }
    val usdFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        currency = Currency.getInstance("USD")
    }

    // --- INICIO DE LA CORRECCIÓN ---

    // 1. Asumimos que el ahorro acumulado está en USD.
    // Calculamos el balance final para cada moneda por separado.
    val finalBalanceUsd = ahorroAcumulado + balanceUsd
    val finalBalanceVes = balanceVes // El balance en VES no se mezcla con el ahorro.

    // 2. Determinamos el color para cada monto de forma independiente.
    val colorUsd = if (finalBalanceUsd >= 0) AccentGreen else AccentRed
    val colorVes = if (finalBalanceVes >= 0) AccentGreen else AccentRed

    val title = if (type == TipoTransaccion.INGRESO) "Ingresos del Mes" else "Gastos del Mes"


    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 3. Mostramos el balance final de USD
            AmountText(amount = usdFormat.format(finalBalanceUsd), color = colorUsd)
            Text(
                "Balance Final en USD (incluye ahorro)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // 4. Mostramos el balance final de VES
            AmountText(amount = vesFormat.format(finalBalanceVes), color = colorVes)
            Text(
                "Balance Neto del Mes en VES",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
    // --- FIN DE LA CORRECCIÓN ---
}

@Composable
private fun AmountText(amount: String, color: Color) {
    Text(
        text = amount,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = color
    )
}