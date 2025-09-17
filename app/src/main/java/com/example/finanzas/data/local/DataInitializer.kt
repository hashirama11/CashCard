package com.example.finanzas.data.local

import android.content.Context
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.model.IconosEstandar
import com.example.finanzas.model.TemaApp
import com.example.finanzas.model.TipoTransaccion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataInitializer @Inject constructor(
    private val monedaDao: MonedaDao,
    private val usuarioDao: UsuarioDao,
    private val categoriaDao: CategoriaDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) {

    companion object {
        // Se actualiza la clave para forzar la re-inicialización de datos con la v10 de la DB.
        const val PREF_KEY_DATA_INITIALIZED = "pref_data_initialized_v10"
    }

    fun initializeDefaultData(context: Context) {
        applicationScope.launch {
            val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val isDataInitialized = sharedPrefs.getBoolean(PREF_KEY_DATA_INITIALIZED, false)

            if (!isDataInitialized) {
                insertInitialMonedas()
                insertInitialUsuario()
                insertInitialCategorias()

                sharedPrefs.edit().putBoolean(PREF_KEY_DATA_INITIALIZED, true).apply()
            }
        }
    }

    private suspend fun insertInitialMonedas() {
        val existingMonedas = monedaDao.getAllMonedas().first().map { it.nombre }.toSet()
        val allDefaultMonedas = listOf(
            Moneda(nombre = "Dólar", simbolo = "$", tasa_conversion = 1.0),
            Moneda(nombre = "Bolívar", simbolo = "Bs.", tasa_conversion = 36.5),
            Moneda(nombre = "Euro", simbolo = "€", tasa_conversion = 0.92),
            Moneda(nombre = "Yen", simbolo = "¥", tasa_conversion = 145.5),
            Moneda(nombre = "Libra Esterlina", simbolo = "£", tasa_conversion = 0.79),
            Moneda(nombre = "Yuan", simbolo = "¥", tasa_conversion = 100.0),
            Moneda(nombre = "Rublo", simbolo = "₽", tasa_conversion = 1.0),
            Moneda(nombre = "Won", simbolo = "₩", tasa_conversion = 1.0),
            Moneda(nombre = "Dólar Australiano", simbolo = "$", tasa_conversion = 1.57),
            Moneda(nombre = "Dólar Hong Kong", simbolo = "$", tasa_conversion = 7.79),
            Moneda(nombre = "Dólar de Macao", simbolo = "$", tasa_conversion = 15.65),
            Moneda(nombre = "Peso Argentino", simbolo = "$", tasa_conversion = 100.0),
            Moneda(nombre = "Franco Suizo", simbolo = "CHF", tasa_conversion = 0.93),
            Moneda(nombre = "Real Brasileño", simbolo = "R$", tasa_conversion = 4.99),
            Moneda(nombre = "Dólar Canadiense", simbolo = "$", tasa_conversion = 1.34),
            Moneda(nombre = "Peso Chileno", simbolo = "$", tasa_conversion = 740.0),
            Moneda(nombre = "Peso Colombiano", simbolo = "$", tasa_conversion = 3950.0),
            Moneda(nombre = "Peso Uruguayo", simbolo = "$", tasa_conversion = 41.0)
        )

        // Filtrar para obtener solo las monedas que no existen y luego insertarlas.
        val monedasToInsert = allDefaultMonedas.filter { !existingMonedas.contains(it.nombre) }
        monedasToInsert.forEach { monedaDao.insertMoneda(it) }
    }

    private suspend fun insertInitialUsuario() {
        // Asumiendo que `getUsuario()` devuelve `Flow<Usuario?>` o `suspend fun getUsuario(): Usuario?`
        // Si tienes un Flow, usa .firstOrNull() para esperar el primer valor
        // Si no existe, lo insertamos.
        val existingUser = usuarioDao.getUsuario().firstOrNull() // O usuarioDao.getUsuario().firstOrNull() si es Flow
        if (existingUser == null) {
            usuarioDao.upsertUsuario(
                Usuario(
                    nombre = "Usuario",
                    email = null,
                    fechaNacimiento = null,
                    monedaPrincipal = "Dólar", // Asegúrate de que esta moneda exista
                    monedaSecundaria = "Bolívar", // Asegúrate de que esta moneda exista
                    tema = TemaApp.CLARO.name,
                    onboardingCompletado = false,
                    ahorroAcumulado = 0.0,
                    objetivoAhorroMensual = 0.0,
                    fechaUltimoCierre = Date().time
                )
            )
        }
    }

    private suspend fun insertInitialCategorias() {
        // Similar, verifica si las categorías ya existen
        // Usamos .first() en el Flow para obtener el primer valor y comprobar si está vacío.
        if (categoriaDao.getAllCategorias().first().isEmpty()) {
            categoriaDao.insertCategoria(
                Categoria(
                    nombre = "Ingreso General",
                    icono = IconosEstandar.OTROS.name,
                    tipo = TipoTransaccion.INGRESO.name,
                    esPersonalizada = false
                )
            )

            val categoriasDeGastos = IconosEstandar.values().map { icono ->
                Categoria(
                    nombre = icono.name.replace('_', ' ').lowercase()
                        .replaceFirstChar { it.uppercase() },
                    icono = icono.name,
                    tipo = TipoTransaccion.GASTO.name,
                    esPersonalizada = false
                )
            }
            categoriasDeGastos.forEach { categoriaDao.insertCategoria(it) }
        }
    }
}