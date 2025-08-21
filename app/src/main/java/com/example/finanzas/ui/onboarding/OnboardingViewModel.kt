package com.example.finanzas.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.repository.FinanzasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: FinanzasRepository
): ViewModel() {

    fun completeOnboarding(name: String, email: String, birthDate: Date?) {
        viewModelScope.launch {
            val currentUser = repository.getUsuario().first()
            currentUser?.let {
                val updatedUser = it.copy(
                    nombre = name,
                    email = email,
                    fechaNacimiento = birthDate?.time,
                    onboardingCompletado = true
                )
                repository.upsertUsuario(updatedUser)
            }
        }
    }
}