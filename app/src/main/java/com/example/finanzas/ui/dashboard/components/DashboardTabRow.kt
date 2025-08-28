package com.example.finanzas.ui.dashboard.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DashboardTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val titles = listOf("Ingresos", "Gastos", "Resumen", "Ahorro")
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(text = title) }
            )
        }
    }
}