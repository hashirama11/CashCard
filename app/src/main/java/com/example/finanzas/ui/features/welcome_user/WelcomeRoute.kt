package com.example.finanzas.ui.features.welcome_user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WelcomeRoute(
    viewModel: WelcomeViewModel = hiltViewModel()

){
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getUser()

    }

    WelcomeScreen(state = uiState)

}