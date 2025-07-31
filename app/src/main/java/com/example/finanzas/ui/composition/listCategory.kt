package com.example.finanzas.ui.composition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ListCategory(){

}


@Composable
fun CategoryGasto(){


    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF0078D4), // Azul Windows 11
            Color(0xFF99CCFF), // Azul pastel
            Color(0xFFE6F2FF)  // Azul muy claro
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .background(brush = gradientBrush, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            OutlinedButton(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(12.dp),
                border = null,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues()
            ) {
                Text(
                    text = "Todos",
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

}

@Composable
@Preview
fun ListCategoryPreview(){
    ListCategory()
}


@Composable
@Preview
fun CategoryGastoPreview(){
    CategoryGasto()
}