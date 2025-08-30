package com.example.finanzas.model

import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Moneda
import com.example.finanzas.data.local.entity.Transaccion

data class TransactionWithDetails(
    val transaccion: Transaccion,
    val categoria: Categoria?,
    val moneda: Moneda?
)