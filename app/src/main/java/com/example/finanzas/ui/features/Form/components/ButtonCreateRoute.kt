package com.example.finanzas.ui.features.Form.components

import androidx.compose.runtime.Composable

@Composable
fun ButtonCreateRoute(state : ButtonCreateUiState, onButtonClicked: () -> Unit){
    val text = state.text
    ButtonCreateScreen(state = ButtonCreateUiState(text = text, enabled = true), onButtonClicked = onButtonClicked)
}