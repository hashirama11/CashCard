package com.example.finanzas.ui.features.Form

import androidx.compose.runtime.Composable
import com.example.finanzas.ui.features.CreateGasto.FormViewModel
import com.example.finanzas.ui.features.CreateGasto.FormScreen


@Composable
fun FormRoute(viewModel: FormViewModel) {

    val descripcion = viewModel.descripcion
    val categoriaSeleccionada = viewModel.categoriaSeleccionada
    val monto = viewModel.monto
    val fecha = viewModel.fecha

    FormScreen(
        state = FormUiState(
            descripcion = descripcion,
            categoriaSeleccionada = categoriaSeleccionada,
            monto = monto,
        ),
        onDescripcionChange = { viewModel.descripcion = it },
        onCategoriaSeleccionada = { viewModel.categoriaSeleccionada = it },
        onMontoChange = { viewModel.monto = it }
    )

}