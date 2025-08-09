package com.example.finanzas.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finanzas.ui.composition.Card.CardRoute
import com.example.finanzas.ui.composition.CategoryGasto
import com.example.finanzas.ui.composition.GastoCard
import com.example.finanzas.ui.composition.GastoCreate.CrearGastoScreen
import com.example.finanzas.ui.composition.Title.TitleScreen

@Composable
fun ScreenHome(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ){
        TitleScreen()
        Column(
            modifier = Modifier
                .padding(4.dp),
        ) {
            CardRoute()
        }
        CrearGastoScreen()
        Spacer(modifier = Modifier.height(20.dp))
        CategoryGasto()
        Spacer(modifier = Modifier.height(20.dp))
        GastoCard()

    }
}


@Composable
@Preview
fun ScreenHomePreview(){
    ScreenHome()
}