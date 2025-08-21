package com.example.finanzas.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TemaApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FinanzasRepository
) : ViewModel() {

    val userState = repository.getUsuario()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateTheme(tema: TemaApp) {
        viewModelScope.launch {
            val currentUser = userState.value ?: return@launch
            repository.upsertUsuario(currentUser.copy(tema = tema.name))
        }
    }


}