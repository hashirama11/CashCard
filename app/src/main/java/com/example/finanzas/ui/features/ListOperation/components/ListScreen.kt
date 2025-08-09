package com.example.finanzas.ui.features.ListOperation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.model.categoria.Categoria




@Composable
fun CategoryRail(
    selected: Categoria?, // null = "TODOS"
    onCategorySelected: (Categoria?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        // Botón para TODOS
        item {
            CategoryButton(
                text = "TODOS",
                onClick = { onCategorySelected(null) },
                selected = selected == null
            )
        }

        // Botones para cada categoría del enum
        items(Categoria.values()) { categoria ->
            val isSelected = categoria == selected
            CategoryButton(
                text = categoria.name,
                onClick = { onCategorySelected(categoria) },
                selected = isSelected
            )
        }
    }
}


@Composable
fun CategoryButton(
    text: String,
    onClick: () -> Unit,
    selected: Boolean = false
) {
    val gradientBrush = Brush.horizontalGradient(
        colors = if (selected) {
            listOf(Color(0xFF005A9E), Color(0xFF0078D4), Color(0xFF99CCFF))
        } else {
            listOf(Color(0xFF0078D4), Color(0xFF99CCFF), Color(0xFFE6F2FF))
        }
    )

    Box(
        modifier = Modifier
            .height(48.dp)
            .background(brush = gradientBrush, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            border = if (selected) BorderStroke(1.dp, Color.White) else null,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues()
        ) {
            Text(
                text = text,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}


@Composable
fun CategoryGasto() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        item {
            CategoryButton(
                text = "TODOS",
                onClick = { /* Acción para TODOS */ }
            )
        }

        items(Categoria.values()) { categoria ->
            CategoryButton(
                text = categoria.name,
                onClick = { /* Acción para esta categoría */ }
            )
        }
    }
}

@Preview
@Composable
fun CategoryGastoPreview() {
    CategoryGasto()
}