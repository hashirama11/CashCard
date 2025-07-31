package com.example.finanzas.ui.composition

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.R

@Composable
fun TitleApp(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Box(
            modifier = Modifier
                .background(Color.White),
        ){
            Text(
                modifier = Modifier,
                text = "Mis gastos",
                color = Color.Black,
                fontSize = 28.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .background(Color.White)
        ){
            Icon(
                painter = painterResource(id = R.drawable.settings_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
                contentDescription = "Settings",
                modifier = Modifier
                    .size(28.dp)
            )
        }

    }
}


@Composable
@Preview
fun TitleAppPreview(){
    TitleApp()
}