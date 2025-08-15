package com.example.finanzas.ui.features.welcome_user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WelcomeUiState())
    val state: StateFlow<WelcomeUiState> = _state

    fun getUser() {
        viewModelScope.launch {
            userRepository.obtenerTodosLosUsuarios().collect { usuarios ->
                val nombre = usuarios.firstOrNull()?.name ?: "Invitado"
                _state.value = _state.value.copy(user = nombre)
            }
        }
    }

}