package com.example.finanzas.ui.transaction_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.EstadoTransaccion
import com.example.finanzas.model.Moneda
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.theme.AccentGreen
import com.example.finanzas.ui.theme.AccentRed
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val transactionDetails by viewModel.transactionDetails.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la Transacción") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        transactionDetails?.let { details ->
            val transaction = details.transaccion
            val category = details.categoria
            val isIncome = transaction.tipo == TipoTransaccion.INGRESO.name
            val amountColor = if (isIncome) AccentGreen else AccentRed
            val currencyFormat = NumberFormat.getCurrencyInstance().apply {
                currency = Currency.getInstance(if (transaction.moneda == Moneda.USD.name) "USD" else "VES")
                if (transaction.moneda == Moneda.VES.name) {
                    maximumFractionDigits = 2
                    (this as java.text.DecimalFormat).decimalFormatSymbols = java.text.DecimalFormatSymbols(Locale("es", "VE")).apply {
                        currencySymbol = "Bs."
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Monto y descripción
                Text(
                    text = currencyFormat.format(transaction.monto),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = amountColor
                )
                Text(
                    text = transaction.descripcion,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // Fila de detalles
                DetailRow(label = "Categoría", value = category?.nombre ?: "Sin categoría")
                DetailRow(label = "Fecha", value = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "VE")).format(transaction.fecha))
                // --- NUEVOS DETALLES ---
                DetailRow(label = "Moneda", value = transaction.moneda)
                DetailRow(label = "Estado", value = transaction.estado.replaceFirstChar { it.uppercase() })

                Spacer(modifier = Modifier.weight(1f))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { onEditClick(transaction.id) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Editar") }
                    Button(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Eliminar") }
                }
            }
        }
    }

    if (showDeleteDialog) {
        // ... (código del diálogo de eliminación sin cambios)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}