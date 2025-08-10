package com.example.finanzas.ui.features.ListOperation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.R
import com.example.finanzas.model.gasto.GastoEntity


@Composable
fun OperacionScreen(
    gasto: GastoEntity,
    @DrawableRes icon: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono con ancho fijo
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(28.dp)
                .padding(end = 8.dp)
        )

        // Columna central ocupa todo el espacio disponible
        Column(
            modifier = Modifier
                .weight(1f) // Se expande para ocupar lo que queda
        ) {
            Text(
                text = gasto.categoria.orEmpty(),
                color = Color.Black,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = gasto.descripcion.orEmpty(),
                color = Color.Black,
                fontSize = 8.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Columna de monto y fecha con ancho fijo
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = gasto.monto?.toString().orEmpty(),
                color = Color.Black,
                fontSize = 16.sp
            )
            Text(
                text = gasto.fecha.toString(),
                color = Color.Black,
                fontSize = 8.sp
            )
        }
    }

}


@Composable
@Preview
fun OperacionScreenPreview(){
    // OperacionScreen()
}