package com.example.finanzas.ui.add_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion
import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.model.EstadoTransaccion
import android.content.Context
import android.net.Uri
import com.example.finanzas.model.TipoTransaccion
import com.example.finanzas.model.UserMessage
import com.example.finanzas.notifications.AlarmScheduler
import com.example.finanzas.util.saveImageToInternalStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: FinanzasRepository,
    private val alarmScheduler: AlarmScheduler,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionState())
    val state = _state.asStateFlow()

    private val transactionId: Int = savedStateHandle.get<String>("transactionId")?.toIntOrNull() ?: -1

    init {
        _state.update { it.copy(isEditing = transactionId != -1) }

        viewModelScope.launch {
            val categories = repository.getAllCategorias().first()
            val currencies = repository.getAllMonedas().first()
            _state.update { it.copy(allCategories = categories, currencies = currencies, selectedCurrency = currencies.firstOrNull()) }

            if (_state.value.isEditing) {
                val transactionToEdit = repository.getTransaccionById(transactionId).first()
                transactionToEdit?.let { tx ->
                    val category = categories.find { it.id == tx.categoriaId }
                    val currency = currencies.find { it.nombre == tx.moneda }
                    _state.update {
                        it.copy(
                            amount = tx.monto.toString(),
                            description = tx.descripcion,
                            selectedTransactionType = TipoTransaccion.valueOf(tx.tipo),
                            selectedCategory = category,
                            transactionDate = tx.fecha,
                            selectedCurrency = currency,
                            isPending = tx.estado == EstadoTransaccion.PENDIENTE.name,
                            completionDate = tx.fechaConcrecion,
                            tipoCompra = tx.tipoCompra,
                            imageUri = tx.imageUri
                        )
                    }
                }
            }
            filterCategories(_state.value.selectedTransactionType)
        }
    }

    fun onAmountChange(newAmount: String) { _state.update { it.copy(amount = newAmount) } }
    fun onDescriptionChange(newDescription: String) { _state.update { it.copy(description = newDescription) } }
    fun onCategorySelected(category: Categoria) { _state.update { it.copy(selectedCategory = category) } }
    fun onCurrencySelected(currency: Moneda) { _state.update { it.copy(selectedCurrency = currency) } }
    fun onPendingStatusChange(isPending: Boolean) { _state.update { it.copy(isPending = isPending) } }
    fun onCompletionDateChange(date: Date?) { _state.update { it.copy(completionDate = date) } }
    fun onTipoCompraSelected(tipo: String) { _state.update { it.copy(tipoCompra = tipo) } }
    fun onImageUriSelected(uriString: String?) {
        if (uriString == null) return

        val contentUri = Uri.parse(uriString)
        saveImageToInternalStorage(context, contentUri)
            .onSuccess { permanentUri ->
                _state.update { it.copy(imageUri = permanentUri.toString()) }
            }
            .onFailure { exception ->
                val message = UserMessage(message = "Error al guardar la imagen: ${exception.message}")
                _state.update { it.copy(userMessages = it.userMessages + message) }
            }
    }

    fun userMessageShown(messageId: Long) {
        _state.update { current ->
            val messages = current.userMessages.filterNot { it.id == messageId }
            current.copy(userMessages = messages)
        }
    }

    fun onTransactionTypeSelected(type: TipoTransaccion) {
        _state.update { it.copy(selectedTransactionType = type) }
        filterCategories(type)
    }

    private fun filterCategories(type: TipoTransaccion) {
        val categoryType = when (type) {
            TipoTransaccion.AHORRO -> TipoTransaccion.INGRESO.name
            TipoTransaccion.COMPRA -> TipoTransaccion.GASTO.name
            else -> type.name
        }
        val filtered = _state.value.allCategories.filter { it.tipo == categoryType }
        val currentCategory = _state.value.selectedCategory
        val isCurrentCategoryStillValid = filtered.any { it.id == currentCategory?.id }

        _state.update {
            it.copy(
                filteredCategories = filtered,
                selectedCategory = if (_state.value.isEditing && isCurrentCategoryStillValid) currentCategory else filtered.firstOrNull()
            )
        }
    }

    fun saveTransaction() {
        val currentState = _state.value
        val amountDouble = currentState.amount.toDoubleOrNull() ?: 0.0

        val isCategoryRequired = currentState.selectedTransactionType == TipoTransaccion.INGRESO || currentState.selectedTransactionType == TipoTransaccion.GASTO
        if (amountDouble <= 0 || currentState.description.isBlank() || (isCategoryRequired && currentState.selectedCategory == null)) {
            return
        }

        // If the transaction is pending, use the selected completion date. Otherwise, it's null.
        val completionDateTime: Date? = if (currentState.isPending) {
            currentState.completionDate
        } else {
            null
        }

        val transactionToSave = Transaccion(
            id = if (currentState.isEditing) transactionId else 0,
            monto = amountDouble,
            moneda = currentState.selectedCurrency?.nombre ?: "",
            descripcion = currentState.description,
            fecha = if (currentState.isEditing) currentState.transactionDate ?: Date() else Date(),
            tipo = currentState.selectedTransactionType.name,
            estado = if (currentState.isPending) EstadoTransaccion.PENDIENTE.name else EstadoTransaccion.CONCRETADO.name,
            categoriaId = currentState.selectedCategory?.id,
            fechaConcrecion = completionDateTime,
            tipoCompra = currentState.tipoCompra,
            imageUri = currentState.imageUri
        )

        viewModelScope.launch {
            if (currentState.isEditing) {
                repository.updateTransaction(transactionToSave)
            } else {
                repository.insertTransaccion(transactionToSave)
            }
            // El resto de la lógica de guardado no cambia
            if (transactionToSave.estado == EstadoTransaccion.PENDIENTE.name && transactionToSave.fechaConcrecion != null) {
                alarmScheduler.schedule(transactionToSave)
            } else {
                alarmScheduler.cancel(transactionToSave)
            }
        }
    }
}