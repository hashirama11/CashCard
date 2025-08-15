package com.example.finanzas.ui.features.dashboard_home

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

@Composable
fun DashboardRoute(onAddGastoClick: () -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddGastoClick,
                containerColor = Color(0xFF43A047),
                shape = CircleShape
            ) {
                Icon(
                    painterResource(id = R.drawable.add),
                    contentDescription = "Agregar gasto",
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        // Aquí va tu contenido del dashboard
        Box(modifier = Modifier.padding(padding)) {
            Text("Pantalla Dashboard")
        }
    }
}
