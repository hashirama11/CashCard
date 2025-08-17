package com.example.finanzas.ui.features.transacciones.cumplidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.R

@Composable
fun TransactionItemGreen(
    state : TransaccionCumplidaUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.wallet),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(state.categoria, fontWeight = FontWeight.Bold)
                Text(state.fecha, color = Color.Gray, fontSize = 12.sp)
            }
        }
        Text("-" + state.monto.toString(), color = Color.Red, fontWeight = FontWeight.Bold)
    }
}

@Composable
@Preview
fun TransactionItemGreenPreview(){
    TransactionItemGreen(
        state = TransaccionCumplidaUiState(
            categoria = "Mercado",
            monto = 230.50,
            fecha = "21 Sep, 03:02 PM",
            icono = R.drawable.wallet
        )
    )
}