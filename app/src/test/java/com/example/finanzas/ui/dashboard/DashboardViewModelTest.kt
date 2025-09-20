@file:OptIn(ExperimentalCoroutinesApi::class)
package com.example.finanzas.ui.dashboard

import app.cash.turbine.test
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.TipoTransaccion
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class DashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FinanzasRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        // Mock all repository flows to return empty flows by default
        every { repository.getAllTransacciones() } returns flowOf(emptyList())
        every { repository.getAllCategorias() } returns flowOf(emptyList())
        every { repository.getUsuario() } returns flowOf(null)
        every { repository.getAllMonedas() } returns flowOf(emptyList())
    }

    private fun createViewModel() {
        viewModel = DashboardViewModel(repository)
    }

    @Test
    fun `initial state is loading`() = runTest {
        // Given
        every { repository.getUsuario() } returns flowOf(null) // No user yet

        // When
        createViewModel()

        // Then
        assertEquals(true, viewModel.state.value.isLoading)
    }

    @Test
    fun `state is updated with correct totals when data is emitted`() = runTest {
        // Given
        val user = Usuario(
            id = 1,
            nombre = "Test",
            email = null,
            fechaNacimiento = null,
            monedaPrincipal = "USD",
            monedaSecundaria = null,
            tema = "CLARO"
        )
        val usd = Moneda(id = 1, nombre = "USD", simbolo = "$")
        val categories = listOf(
            Categoria(id = 1, nombre = "Salario", icono = "SALARY", tipo = TipoTransaccion.INGRESO.name),
            Categoria(id = 2, nombre = "Comida", icono = "FOOD", tipo = TipoTransaccion.GASTO.name)
        )
        val transactions = listOf(
            Transaccion(id = 1, monto = 2000.0, moneda = "USD", tipo = TipoTransaccion.INGRESO.name, categoriaId = 1, fecha = Date(), descripcion = "", estado = "", imageUri = null, tipoCompra = null, fechaConcrecion = null),
            Transaccion(id = 2, monto = 150.0, moneda = "USD", tipo = TipoTransaccion.GASTO.name, categoriaId = 2, fecha = Date(), descripcion = "", estado = "", imageUri = null, tipoCompra = null, fechaConcrecion = null)
        )

        every { repository.getUsuario() } returns flowOf(user)
        every { repository.getAllMonedas() } returns flowOf(listOf(usd))
        every { repository.getAllCategorias() } returns flowOf(categories)
        every { repository.getAllTransacciones() } returns flowOf(transactions)

        // When
        createViewModel()
        viewModel.onCurrencySelected(usd) // Select a currency to trigger calculation

        // Then
        viewModel.state.test {
            val state = awaitItem()
            // May see initial loading state first, so we might need to skip
            val finalState = if (state.isLoading) awaitItem() else state

            assertEquals(false, finalState.isLoading)
            assertEquals(2000.0, finalState.totalIngresos, 0.0)
            assertEquals(150.0, finalState.totalGastos, 0.0)
            assertEquals(1850.0, finalState.balanceNeto, 0.0)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
