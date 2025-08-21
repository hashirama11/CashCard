package com.example.finanzas.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import com.example.finanzas.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
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

    // --- MEJORA: El estado se gestiona aquí, en el nivel superior ---
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf<Date?>(null) }

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
                    onBirthDateChange = { birthDate = it }
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
                    viewModel.completeOnboarding("Usuario", "", null)
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
                            birthDate
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .size(200.dp),
            painter = painterResource(id = onboardingPage.image),
            contentDescription = onboardingPage.title
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
    onBirthDateChange: (Date?) -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }

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