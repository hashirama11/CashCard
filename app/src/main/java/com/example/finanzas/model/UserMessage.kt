package com.example.finanzas.model

import java.util.UUID

data class UserMessage(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val message: String
)
