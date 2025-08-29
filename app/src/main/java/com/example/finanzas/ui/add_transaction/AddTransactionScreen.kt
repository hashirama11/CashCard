package com.example.finanzas.ui.add_transaction

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.util.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        viewModel.onImageUriSelected(uri.toString())
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showDatePicker.value = true
        } else {
            Toast.makeText(
                context,
                "Permiso de notificaciones necesario para añadir recordatorios.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val descriptionLabel = when (state.selectedTransactionType) {
        TipoTransaccion.INGRESO -> "Descripción (ej: Salario, Venta)"
        TipoTransaccion.GASTO -> "Descripción (ej: Café en la panadería)"
        TipoTransaccion.COMPRA -> "Descripción (ej: Compra de supermercado)"
        TipoTransaccion.AHORRO -> "Descripción (ej: Ahorro para vacaciones)"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Editar Transacción" else "Añadir Transacción") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.saveTransaction()
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                Text(if (state.isEditing) "Guardar Cambios" else "Guardar Transacción")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TabRow(selectedTabIndex = state.selectedTransactionType.ordinal) {
                Tab(
                    selected = state.selectedTransactionType == TipoTransaccion.INGRESO,
                    onClick = { viewModel.onTransactionTypeSelected(TipoTransaccion.INGRESO) },
                    text = { Text("Ingreso") }
                )
                Tab(
                    selected = state.selectedTransactionType == TipoTransaccion.GASTO,
                    onClick = { viewModel.onTransactionTypeSelected(TipoTransaccion.GASTO) },
                    text = { Text("Gasto") }
                )
                Tab(
                    selected = state.selectedTransactionType == TipoTransaccion.COMPRA,
                    onClick = { viewModel.onTransactionTypeSelected(TipoTransaccion.COMPRA) },
                    text = { Text("Compra") }
                )
                Tab(
                    selected = state.selectedTransactionType == TipoTransaccion.AHORRO,
                    onClick = { viewModel.onTransactionTypeSelected(TipoTransaccion.AHORRO) },
                    text = { Text("Ahorro") }
                )
            }

            var currencyExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = !currencyExpanded }
            ) {
                OutlinedTextField(
                    value = state.selectedCurrency?.nombre ?: "Seleccione Moneda",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Moneda") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = currencyExpanded,
                    onDismissRequest = { currencyExpanded = false }
                ) {
                    state.currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(currency.nombre) },
                            onClick = {
                                viewModel.onCurrencySelected(currency)
                                currencyExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.onAmountChange(it) },
                label = { Text("Monto") },
                leadingIcon = { Text(state.selectedCurrency?.simbolo ?: "", style = MaterialTheme.typography.bodyLarge) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text(descriptionLabel) },
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(visible = state.selectedTransactionType == TipoTransaccion.COMPRA) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { viewModel.onTipoCompraSelected("Factura") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.tipoCompra == "Factura") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text("Factura")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { viewModel.onTipoCompraSelected("Producto") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.tipoCompra == "Producto") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text("Producto")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Seleccionar Imagen")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Imagen de la compra",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.selectedCategory?.nombre ?: "Seleccionar categoría",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.filteredCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.nombre) },
                            onClick = {
                                viewModel.onCategorySelected(category)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("¿Marcar como pendiente?", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = state.isPending,
                    onCheckedChange = { viewModel.onPendingStatusChange(it) }
                )
            }

            AnimatedVisibility(visible = state.isPending) {
                OutlinedTextField(
                    value = state.completionDate?.let { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha de recordatorio (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                when (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)) {
                                    PackageManager.PERMISSION_GRANTED -> showDatePicker.value = true
                                    else -> permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            } else {
                                showDatePicker.value = true
                            }
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha y hora")
                        }
                    }
                )
            }
        }
    }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onCompletionDateChange(Date(it + 86400000)) // Add one day to avoid timezone issues
                        showTimePicker.value = true
                    }
                    showDatePicker.value = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker.value) {
        TimePickerDialog(
            context = context,
            onCancel = { showTimePicker.value = false },
            onConfirm = { hour, minute ->
                val calendar = Calendar.getInstance().apply {
                    time = state.completionDate ?: Date()
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                viewModel.onCompletionDateChange(calendar.time)
                showTimePicker.value = false
            }
        )
    }
}