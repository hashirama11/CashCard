package com.example.finanzas.ui.features.profile.category.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import com.example.finanzas.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun CategoryCreateScreen(
    modifier: Modifier = Modifier,
    nombre: String,
    onNombreChanged: (String) -> Unit,
    icono: Int,
    onGuardarClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear nueva categoría",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo nombre categoría
        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChanged,
            label = { Text("Nombre de la categoría") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selección de ícono (mock por ahora)
        Card(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
            elevation = CardDefaults.cardElevation(4.dp),
            onClick = { /* más adelante: abrir selección de íconos */ }
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    painter = painterResource(id = R.drawable.chart),
                    contentDescription = "Seleccionar ícono",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón guardar
        Button(
            onClick = onGuardarClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar categoría", color = Color.White)
        }
    }
}

@Composable
@Preview
fun CategoryCreateScreenPreview() {
    CategoryCreateScreen(
        nombre = "Nueva Categoría",
        onNombreChanged = {},
        icono = R.drawable.chart,
        onGuardarClick = {}
    )
}

