package com.example.finanzas.ui.features.welcome_user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.R
import com.example.finanzas.ui.screen.CircleButtonGreen

// Compose Screen for Welcome User
@Composable
fun WelcomeScreen(
    state : WelcomeUiState = WelcomeUiState()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(state.version, fontSize = 16.sp, color = Color.Gray)
            Text("Bienvenida de nuevo" + " " + state.user + " " + state.mensaje, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
        }
    }
}

// Preview del Compose Screen
@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        state = WelcomeUiState()
    )
}