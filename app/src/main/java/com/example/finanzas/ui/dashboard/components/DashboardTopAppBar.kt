package com.example.finanzas.ui.dashboard.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopAppBar(userName: String, onPurchaseHistoryClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Hola, $userName",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = onPurchaseHistoryClick) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Historial de Compras"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}