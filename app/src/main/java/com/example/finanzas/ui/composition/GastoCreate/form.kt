package com.example.finanzas.ui.composition.GastoCreate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finanzas.model.categoria.Categorias
import com.example.finanzas.viewmodel.CrearGastoViewModel
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
fun FormApp(viewModel: CrearGastoViewModel) {

    var categoriaSeleccionada by mutableStateOf(Categorias.OTROS)

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        TextField(
            value = viewModel.descripcion,
            onValueChange = { viewModel.descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        SelectorCategoria(
            categoria = viewModel.categoriaSeleccionada,
            onCategoriaSeleccionada = { viewModel.categoriaSeleccionada = it }
        )

        TextField(
            value = viewModel.monto,
            onValueChange = { viewModel.monto = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorCategoria(
    categoria: Categorias,
    onCategoriaSeleccionada: (Categorias) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = categoria.name.lowercase().replaceFirstChar { it.uppercase() },
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Categorias.values().forEach { categoriaEnum ->
                DropdownMenuItem(
                    text = { Text(categoriaEnum.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    onClick = {
                        onCategoriaSeleccionada(categoriaEnum)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun FormAppPreview(){

}