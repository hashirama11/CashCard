package com.example.finanzas.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finanzas.ui.composition.CardFull
import com.example.finanzas.ui.composition.TitleApp

@Composable
fun ScreenHome(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ){
        TitleApp()

        CardFull()

    }
}


@Composable
@Preview
fun ScreenHomePreview(){
    ScreenHome()
}