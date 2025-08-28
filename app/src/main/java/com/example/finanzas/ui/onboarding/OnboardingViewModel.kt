package com.example.finanzas.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.repository.FinanzasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    private val _monedas = MutableStateFlow<List<Moneda>>(emptyList())
    val monedas = _monedas.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllMonedas().collect {
                _monedas.value = it
            }
        }
    }

    fun completeOnboarding(
        name: String,
        email: String,
        birthDate: Date?,
        monedaPrincipal: String,
        monedaSecundaria: String?
    ) {
        viewModelScope.launch {
            val currentUser = repository.getUsuario().first()
            currentUser?.let {
                val updatedUser = it.copy(
                    nombre = name,
                    email = email,
                    fechaNacimiento = birthDate?.time,
                    monedaPrincipal = monedaPrincipal,
                    monedaSecundaria = monedaSecundaria,
                    onboardingCompletado = true
                )
                repository.upsertUsuario(updatedUser)
            }
        }
    }
}