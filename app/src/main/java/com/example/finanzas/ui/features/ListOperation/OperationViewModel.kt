package com.example.finanzas.ui.features.ListOperation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.repository.GastoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperationViewModel @Inject constructor(
    private val repository: GastoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OperactionUiState())

    val uiState: StateFlow<OperactionUiState> = _uiState

    fun obtenerGastosPorCategoria(categoria: String) {
        viewModelScope.launch {
            repository.obtenerGastosPorCategoria(categoria)
                .collect { list ->
                    _uiState.value = _uiState.value.copy(gastos = list)
                }
        }
    }

}