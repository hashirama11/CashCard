package com.example.finanzas.ui.features.CreateGasto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel



@Composable
fun CrearGastoScreen(viewModel: CrearGastoViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FormApp(viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        CreateGasto(viewModel)
        viewModel.mensajeConfirmacion?.let {
            Text(it, color = Color.Green, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
@Preview
fun CrearGastoScreenPreview(){
    CrearGastoScreen()
}