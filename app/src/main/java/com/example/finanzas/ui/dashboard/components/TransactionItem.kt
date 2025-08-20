package com.example.finanzas.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.R // Asegúrate de tener un ícono 'wallet' en tus drawables
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionItem(transaction: Transaccion) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "VE"))
    val amountColor = if (transaction.tipo == TipoTransaccion.INGRESO.name) AccentGreen else AccentRed
    val sign = if (transaction.tipo == TipoTransaccion.INGRESO.name) "+" else "-"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wallet), // Placeholder
                contentDescription = "Categoría",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Descripción y Fecha
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(transaction.fecha),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // Monto
        Text(
            text = "$sign${currencyFormat.format(transaction.monto)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}