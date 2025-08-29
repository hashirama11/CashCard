package com.example.finanzas.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.finanzas.R
import com.example.finanzas.data.local.entity.Moneda
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val pages = listOf(
        OnboardingPage.First,
        OnboardingPage.Second,
        OnboardingPage.Third
    )
    val pagerState = rememberPagerState(pageCount = { pages.size + 1 }) // +1 para la pantalla de datos
    val coroutineScope = rememberCoroutineScope()
    val monedas by viewModel.monedas.collectAsState()

    // --- MEJORA: El estado se gestiona aquí, en el nivel superior ---
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf<Date?>(null) }
    var primaryCurrency by remember { mutableStateOf<Moneda?>(null) }
    var secondaryCurrency by remember { mutableStateOf<Moneda?>(null) }


    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(10f),
            userScrollEnabled = false // El usuario solo avanza con el botón
        ) { page ->
            if (page < pages.size) {
                PagerScreen(onboardingPage = pages[page])
            } else {
                // Pasamos los valores y los lambdas para actualizarlos
                UserDataScreen(
                    name = name,
                    onNameChange = { name = it },
                    email = email,
                    onEmailChange = { email = it },
                    birthDate = birthDate,
                    onBirthDateChange = { birthDate = it },
                    monedas = monedas,
                    primaryCurrency = primaryCurrency,
                    onPrimaryCurrencyChange = { primaryCurrency = it },
                    secondaryCurrency = secondaryCurrency,
                    onSecondaryCurrencyChange = { secondaryCurrency = it }
                )
            }
        }

        Row(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para saltar, visible solo en las primeras páginas
            AnimatedVisibility(visible = pagerState.currentPage < pages.size) {
                TextButton(onClick = {
                    // Al saltar, guardamos con los valores por defecto y finalizamos
                    viewModel.completeOnboarding("Usuario", "", null, "Dólar", "Bolívar")
                    onFinish()
                }) {
                    Text("Saltar")
                }
            }
            // Espaciador para centrar los otros elementos si "Saltar" no es visible
            if (pagerState.currentPage >= pages.size) {
                Spacer(modifier = Modifier.width(64.dp))
            }


            // Indicador de página
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size + 1) { iteration ->
                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(8.dp)
                            .width(if (pagerState.currentPage == iteration) 24.dp else 8.dp)
                            .background(color, shape = RoundedCornerShape(50))
                    )
                }
            }

            // Botón Siguiente/Finalizar
            Button(
                onClick = {
                    if (pagerState.currentPage < pages.size) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        // --- CORRECCIÓN: Guardamos los datos y finalizamos ---
                        viewModel.completeOnboarding(
                            name.ifBlank { "Usuario" }, // Usamos un nombre por defecto si está vacío
                            email,
                            birthDate,
                            primaryCurrency?.nombre ?: "Dólar",
                            secondaryCurrency?.nombre ?: "Bolívar"
                        )
                        onFinish()
                    }
                }
            ) {
                Text(text = if (pagerState.currentPage < pages.size) "Siguiente" else "Finalizar")
            }
        }
    }
}

@Composable
fun PagerScreen(onboardingPage: OnboardingPage) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(onboardingPage.animation))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = onboardingPage.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = onboardingPage.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataScreen(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    birthDate: Date?,
    onBirthDateChange: (Date?) -> Unit,
    monedas: List<Moneda>,
    primaryCurrency: Moneda?,
    onPrimaryCurrencyChange: (Moneda) -> Unit,
    secondaryCurrency: Moneda?,
    onSecondaryCurrencyChange: (Moneda) -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }
    var primaryExpanded by remember { mutableStateOf(false) }
    var secondaryExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "¡Casi listo! Cuéntanos sobre ti",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Tu Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Correo Electrónico (Opcional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        ExposedDropdownMenuBox(
            expanded = primaryExpanded,
            onExpandedChange = { primaryExpanded = !primaryExpanded }
        ) {
            OutlinedTextField(
                value = primaryCurrency?.nombre ?: "Seleccione Moneda Principal",
                onValueChange = {},
                readOnly = true,
                label = { Text("Moneda Principal") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = primaryExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = primaryExpanded,
                onDismissRequest = { primaryExpanded = false }
            ) {
                monedas.forEach { moneda ->
                    DropdownMenuItem(
                        text = { Text(moneda.nombre) },
                        onClick = {
                            onPrimaryCurrencyChange(moneda)
                            primaryExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ExposedDropdownMenuBox(
            expanded = secondaryExpanded,
            onExpandedChange = { secondaryExpanded = !secondaryExpanded }
        ) {
            OutlinedTextField(
                value = secondaryCurrency?.nombre ?: "Seleccione Moneda Secundaria",
                onValueChange = {},
                readOnly = true,
                label = { Text("Moneda Secundaria (Opcional)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = secondaryExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = secondaryExpanded,
                onDismissRequest = { secondaryExpanded = false }
            ) {
                monedas.forEach { moneda ->
                    DropdownMenuItem(
                        text = { Text(moneda.nombre) },
                        onClick = {
                            onSecondaryCurrencyChange(moneda)
                            secondaryExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = birthDate?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de Nacimiento (Opcional)") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showDatePicker.value = true }) {
                    // Usando un icono estándar de Material
                    Icon(painterResource(id = R.drawable.co_present_24dp), contentDescription = "Seleccionar fecha")
                }
            }
        )
    }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            // Se debe sumar un día porque el selector a veces toma la zona horaria UTC
                            val selectedDate = Date(it + 86400000)
                            onBirthDateChange(selectedDate)
                        }
                        showDatePicker.value = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}