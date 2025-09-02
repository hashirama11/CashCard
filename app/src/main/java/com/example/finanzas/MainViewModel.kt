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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: FinanzasRepository
) : ViewModel() {
    private val userFlow = repository.getUsuario()

    val uiState: StateFlow<UserPreferencesState> = userFlow
        .map { user ->
            UserPreferencesState(
                isDarkTheme = user?.tema == TemaApp.OSCURO.name,
                onboardingCompleted = user?.onboardingCompletado
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferencesState()
        )

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    fun onAuthenticationSuccess() {
        _isAuthenticated.value = true
    }
}