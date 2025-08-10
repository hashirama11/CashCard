package com.example.finanzas.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.categoria.Categoria
import com.example.finanzas.ui.features.Card.CardRoute
import com.example.finanzas.ui.features.CreateGasto.FormViewModel
import com.example.finanzas.ui.features.Form.FormRoute
import com.example.finanzas.ui.features.Form.components.ButtonCreateRoute
import com.example.finanzas.ui.features.Form.components.ButtonCreateUiState
import com.example.finanzas.ui.features.ListOperation.OperactionRoute
import com.example.finanzas.ui.features.ListOperation.OperationViewModel
import com.example.finanzas.ui.features.ListOperation.components.CategoryRail
import com.example.finanzas.ui.features.Title.TitleScreen

@Composable
fun ScreenHome(
    operationViewModel: OperationViewModel = hiltViewModel(),
    formViewModel: FormViewModel = hiltViewModel(),
){
    var categoriaSeleccionada by remember { mutableStateOf<Categoria?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.padding(20.dp))
        TitleScreen()
        Spacer(modifier = Modifier.padding(16.dp))
        CardRoute()
        Spacer(modifier = Modifier.padding(16.dp))
        FormRoute(formViewModel)
        Spacer(modifier = Modifier.padding(16.dp))
        ButtonCreateRoute(
            state = ButtonCreateUiState(text = "Crear gasto"),
            onButtonClicked = {
                formViewModel.crearGasto()
            }
        )
        Spacer(modifier = Modifier.padding(20.dp))

        // Ahora OperactionRoute recibe el estado y el callback
        OperactionRoute(
            selectedCategory = categoriaSeleccionada,
            onCategorySelected = { categoriaSeleccionada = it }
        )
    }
}



@Composable
@Preview
fun ScreenHomePreview(){
    ScreenHome()
}