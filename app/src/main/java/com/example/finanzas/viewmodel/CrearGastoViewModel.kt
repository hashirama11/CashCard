package com.example.finanzas.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.finanzas.model.GastoEntity
import com.example.finanzas.repository.GastoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate



@HiltViewModel
class CrearGastoViewModel @Inject constructor(
    private val repository: GastoRepository
) : ViewModel() {

    var descripcion by mutableStateOf("")
    var categoria by mutableStateOf("")
    var monto by mutableStateOf("")
    var fecha by mutableStateOf(LocalDate.now())

    var mensajeConfirmacion by mutableStateOf<String?>(null)

    fun crearGasto() {
        val montoDouble = monto.toDoubleOrNull()
        if (descripcion.isBlank() || categoria.isBlank() || montoDouble == null) {
            mensajeConfirmacion = "❌ Todos los campos son obligatorios"
            return
        }

        val gasto = GastoEntity().apply {
            this.descripcion = descripcion
            this.categoria = categoria
            this.monto = montoDouble
            this.fecha = fecha
        }

        viewModelScope.launch {
            repository.crearGasto(gasto)
            mensajeConfirmacion = "✅ Gasto creado correctamente"
            limpiarCampos()
        }
    }


    private fun limpiarCampos() {
        descripcion = ""
        categoria = ""
        monto = ""
        fecha = LocalDate.now()
    }
}