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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.R
import com.example.finanzas.model.gasto.GastoEntity


@Composable
fun OperacionScreen(
    gasto : GastoEntity,
    @DrawableRes icon: Int
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
        ){
            Icon(
                painter = painterResource(icon),
                contentDescription = "Settings",
                modifier = Modifier
                    .size(28.dp)
            )
        }

        Box(

            modifier = Modifier
                .background(Color.White)
        ){
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = gasto.categoria.toString(),
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = gasto.descripcion.toString(),
                    color = Color.Black,
                    fontSize = 8.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .background(Color.White)
        ){
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = gasto.monto.toString(),
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
}


@Composable
@Preview
fun OperacionScreenPreview(){
    // OperacionScreen()
}