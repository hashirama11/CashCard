package com.example.finanzas.ui.features.CreateGasto

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.model.categoria.Categorias
import com.example.finanzas.model.gasto.GastoEntity
import com.example.finanzas.repository.GastoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CrearGastoViewModel @Inject constructor(
    private val repository: GastoRepository
) : ViewModel() {

    var descripcion by mutableStateOf("")
    var categoriaSeleccionada by mutableStateOf(Categorias.OTROS)
    var monto by mutableStateOf("")
    var fecha by mutableStateOf(LocalDate.now())

    var mensajeConfirmacion by mutableStateOf<String?>(null)

    fun crearGasto() {
        val montoDouble = monto.toDoubleOrNull()

        // Validación actualizada
        if (descripcion.isBlank() || montoDouble == null || categoriaSeleccionada == Categorias.OTROS) {
            mensajeConfirmacion = "❌ Todos los campos son obligatorios"
            return
        }

        val gasto = GastoEntity().apply {
            this.descripcion = descripcion
            this.categoria = categoriaSeleccionada.name // Enum → String
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
        categoriaSeleccionada = Categorias.OTROS
        monto = ""
        fecha = LocalDate.now()
    }
}