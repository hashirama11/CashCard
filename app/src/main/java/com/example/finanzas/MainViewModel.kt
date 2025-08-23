package com.example.finanzas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TemaApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: FinanzasRepository
) : ViewModel() {
    private val userFlow = repository.getUsuario()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isDarkTheme = userFlow
        .map { it?.tema == TemaApp.OSCURO.name }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val onboardingCompleted: StateFlow<Boolean?> = userFlow
        .map { it?.onboardingCompletado }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- NUEVO ESTADO PARA LA AUTENTICACIÓN ---
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    fun onAuthenticationSuccess() {
        _isAuthenticated.value = true
    }
}