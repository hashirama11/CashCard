package com.example.finanzas.ui.composition

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

@Composable
fun GastoCard(){

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
                painter = painterResource(id = R.drawable.shopping_cart_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
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
                    text = "Supermercado",
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "Comida",
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
                    text = "USD 50.00",
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "23 abrr",
                    color = Color.Black,
                    fontSize = 8.sp
                )
            }
        }

    }
}


@Composable
@Preview
fun GastoCardPreview(){
    GastoCard()
}