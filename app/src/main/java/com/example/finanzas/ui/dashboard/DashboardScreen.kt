package com.example.finanzas.ui.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanzas.R
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.ui.dashboard.components.DashboardContent
import com.example.finanzas.ui.dashboard.components.DashboardTabRow
import com.example.finanzas.ui.dashboard.components.DashboardTopAppBar
import com.example.finanzas.ui.theme.AccentGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { DashboardTopAppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Navegar a la pantalla de añadir transacción */ },
                containerColor = AccentGreen
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Añadir Transacción"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Pestañas (Ingresos / Gastos)
            DashboardTabRow(
                selectedTabIndex = pagerState.currentPage,
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )

            // Contenido deslizable (Pager)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> DashboardContent(
                        balance = state.totalIngresos,
                        transactions = state.transacciones.filter { it.tipo == TipoTransaccion.INGRESO.name },
                        type = TipoTransaccion.INGRESO
                    )
                    1 -> DashboardContent(
                        balance = state.totalGastos,
                        transactions = state.transacciones.filter { it.tipo == TipoTransaccion.GASTO.name },
                        type = TipoTransaccion.GASTO
                    )
                }
            }
        }
    }
}