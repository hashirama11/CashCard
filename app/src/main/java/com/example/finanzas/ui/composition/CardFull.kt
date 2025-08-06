package com.example.finanzas.ui.composition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.viewmodel.CrearGastoViewModel
import com.example.finanzas.viewmodel.TotalGastoViewModel

@Composable
fun CardFull(viewModel: TotalGastoViewModel = hiltViewModel()) {
    val total by viewModel.totalGastos.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0078D4),
                            Color(0xFF99CCFF),
                            Color(0xFFE6F2FF)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Total Gastos",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = "USD ${"%,.2f".format(total)}",
                    color = Color.White,
                    fontSize = 36.sp
                )
            }
        }
    }
}

@Composable
@Preview
fun CardFullPreview(){
    CardFull()
}