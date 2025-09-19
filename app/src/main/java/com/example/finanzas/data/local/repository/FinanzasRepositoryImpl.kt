package com.example.finanzas.data.local.repository

import com.example.finanzas.data.local.dao.BudgetDao
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.entity.Budget
import com.example.finanzas.data.local.entity.BudgetCategory
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.model.BudgetCategoryDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import javax.inject.Inject

class FinanzasRepositoryImpl @Inject constructor(
    private val transaccionDao: TransaccionDao,
    private val categoriaDao: CategoriaDao,
    private val usuarioDao: UsuarioDao,
    private val monedaDao: MonedaDao,
    private val budgetDao: BudgetDao
) : FinanzasRepository {

    override fun getAllTransacciones(): Flow<List<Transaccion>> = transaccionDao.getAllTransacciones()
    override suspend fun insertTransaccion(transaccion: Transaccion) = transaccionDao.insertTransaccion(transaccion)
    override suspend fun updateTransaction(transaccion: Transaccion) = transaccionDao.updateTransaccion(transaccion)
    override fun getTransaccionById(id: Int): Flow<Transaccion?> = transaccionDao.getTransaccionById(id)
    override suspend fun deleteTransaccionById(id: Int) = transaccionDao.deleteTransaccionById(id)
    override fun getTransactionsFromLastThreeMonths(): Flow<List<Transaccion>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        return transaccionDao.getTransactionsFromLastThreeMonths(calendar.timeInMillis)
    }

    override fun getAllCategorias(): Flow<List<Categoria>> = categoriaDao.getAllCategorias()
    override suspend fun insertCategoria(categoria: Categoria) = categoriaDao.insertCategoria(categoria)
    override suspend fun deleteCategoria(categoria: Categoria) = categoriaDao.deleteCategoria(categoria)

    override fun getUsuario(): Flow<Usuario?> = usuarioDao.getUsuario()
    override suspend fun upsertUsuario(usuario: Usuario) = usuarioDao.upsertUsuario(usuario)

    override fun getAllMonedas(): Flow<List<Moneda>> = monedaDao.getAllMonedas()
    override suspend fun insertMoneda(moneda: Moneda) = monedaDao.insertMoneda(moneda)
    override suspend fun updateMoneda(moneda: Moneda) = monedaDao.updateMoneda(moneda)

    override suspend fun realizarCorteDeMes() {
        val usuario = usuarioDao.getUsuario().first() ?: return
        val allTransactions = transaccionDao.getAllTransacciones().first()
        val lastCutOff = Calendar.getInstance().apply { timeInMillis = usuario.fechaUltimoCierre }
        val now = Calendar.getInstance()

        if (now.get(Calendar.MONTH) == lastCutOff.get(Calendar.MONTH) && now.get(Calendar.YEAR) == lastCutOff.get(Calendar.YEAR)) {
            // Cut-off for this month has already been done
            return
        }

        val endOfPreviousMonth = Calendar.getInstance().apply {
            timeInMillis = lastCutOff.timeInMillis
            add(Calendar.MONTH, 1)
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.DATE, -1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        val transactionsToProcess = allTransactions.filter {
            val txCalendar = Calendar.getInstance().apply { time = it.fecha }
            txCalendar.after(lastCutOff) && txCalendar.before(endOfPreviousMonth)
        }

        val balancePorMoneda = transactionsToProcess.groupBy { it.moneda }
            .mapValues { (_, transactions) ->
                val ingresos = transactions.filter { it.tipo == "INGRESO" || it.tipo == "AHORRO" }.sumOf { it.monto }
                val gastos = transactions.filter { it.tipo == "GASTO" || it.tipo == "COMPRA" }.sumOf { it.monto }
                ingresos - gastos
            }

        val balancePrincipal = balancePorMoneda[usuario.monedaPrincipal] ?: 0.0
        usuario.ahorroAcumulado += balancePrincipal
        usuario.fechaUltimoCierre = now.timeInMillis
        usuarioDao.upsertUsuario(usuario)
    }

    override fun getBudgetDetails(month: Int, year: Int): Flow<List<BudgetCategoryDetail>> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val startDate = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val endDate = calendar.timeInMillis

        return budgetDao.getBudgetForMonth(month, year).flatMapLatest { budgetWithCategories ->
            if (budgetWithCategories == null) {
                return@flatMapLatest flowOf(emptyList())
            }

            val categoryDetailsFlows = budgetWithCategories.budgetCategories.map { budgetCategory ->
                val categoryFlow = categoriaDao.getCategoriaById(budgetCategory.categoryId)
                val spendingFlow = transaccionDao.getSumOfExpensesForCategory(budgetCategory.categoryId, startDate, endDate)

                combine(categoryFlow, spendingFlow) { category, spending ->
                    category?.let {
                        BudgetCategoryDetail(
                            categoryName = it.nombre,
                            icon = it.icono,
                            budgetedAmount = budgetCategory.budgetedAmount,
                            actualSpending = spending,
                            categoryId = it.id
                        )
                    }
                }
            }

            combine(categoryDetailsFlows) { details ->
                details.filterNotNull().toList()
            }
        }
    }

    override suspend fun saveBudget(budget: Budget, categories: List<BudgetCategory>) {
        budgetDao.saveBudget(budget, categories)
    }
}