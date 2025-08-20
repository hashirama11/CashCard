package com.example.finanzas.model

enum class TipoTransaccion {
    INGRESO,
    GASTO
}

enum class EstadoTransaccion {
    CONCRETADO,
    PENDIENTE
}

enum class TemaApp {
    CLARO,
    OSCURO
}

// Puedes expandir esta lista según necesites
enum class Moneda(val simbolo: String) {
    VES("Bs."),
    USD("$")
}