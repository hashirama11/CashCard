package com.example.finanzas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TemaApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: FinanzasRepository
): ViewModel() {
    // Observamos si el usuario prefiere el modo oscuro
    val isDarkTheme = repository.getUsuario()
        .map { it?.tema == TemaApp.OSCURO.name }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}