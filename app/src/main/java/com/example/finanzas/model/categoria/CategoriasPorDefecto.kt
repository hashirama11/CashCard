package com.example.finanzas.model.categoria

fun categoriasPorDefecto(): List<CategoriaEntity> {
    return listOf(
        // Vivienda
        CategoriaEntity(nombre = "Alquiler", icono = IconosEstandar.ALQUILER.icono),
        CategoriaEntity(nombre = "Hipoteca", icono = IconosEstandar.HIPOTECA.icono),
        CategoriaEntity(nombre = "Agua", icono = IconosEstandar.AGUA.icono),
        CategoriaEntity(nombre = "Gas", icono = IconosEstandar.GAS.icono),
        CategoriaEntity(nombre = "Luz", icono = IconosEstandar.LUZ.icono),
        CategoriaEntity(nombre = "Internet", icono = IconosEstandar.INTERNET.icono),

        // Alimentación
        CategoriaEntity(nombre = "Supermercado", icono = IconosEstandar.SUPERMERCADO.icono),
        CategoriaEntity(nombre = "Restaurantes", icono = IconosEstandar.RESTARURANTES.icono),
        CategoriaEntity(nombre = "Cafeterías", icono = IconosEstandar.CAFETERIAS.icono),
        CategoriaEntity(nombre = "Comida rápida", icono = IconosEstandar.COMIDA_RAPIDA.icono),
        CategoriaEntity(nombre = "Delivery", icono = IconosEstandar.DELIVERY.icono),

        // Transporte
        CategoriaEntity(nombre = "Combustible", icono = IconosEstandar.COMBUSTIBLE.icono),
        CategoriaEntity(nombre = "Transporte público", icono = IconosEstandar.TRASNPORTE_PUBLICO.icono),
        CategoriaEntity(nombre = "Mantenimiento vehículo", icono = IconosEstandar.MANTENIMIENTO_VEHICULO.icono),
        CategoriaEntity(nombre = "Seguros", icono = IconosEstandar.SEGUROS.icono),

        // Salud
        CategoriaEntity(nombre = "Medicina", icono = IconosEstandar.MEDICINA.icono),
        CategoriaEntity(nombre = "Farmacia", icono = IconosEstandar.FARMACIA.icono),
        CategoriaEntity(nombre = "Consultas médicas", icono = IconosEstandar.CONSULTAS_MEDICAS.icono),
        CategoriaEntity(nombre = "Seguros de salud", icono = IconosEstandar.SEGUROS_SALUD.icono),

        // Educación
        CategoriaEntity(nombre = "Cursos", icono = IconosEstandar.CURSOS.icono),
        CategoriaEntity(nombre = "Colegios", icono = IconosEstandar.COLEGIOS.icono),
        CategoriaEntity(nombre = "Universidad", icono = IconosEstandar.UNIVERSIDAD.icono),
        CategoriaEntity(nombre = "Libros", icono = IconosEstandar.LIBROS.icono),
        CategoriaEntity(nombre = "Materiales", icono = IconosEstandar.MATERIALES.icono),
        CategoriaEntity(nombre = "Suscripciones educación", icono = IconosEstandar.SUSCRIPCIONES_EDUCACION.icono),

        // Entretenimiento y Ocio
        CategoriaEntity(nombre = "Cine", icono = IconosEstandar.CINE.icono),
        CategoriaEntity(nombre = "Teatro", icono = IconosEstandar.TEATRO.icono),
        CategoriaEntity(nombre = "Eventos", icono = IconosEstandar.EVENTOS.icono),
        CategoriaEntity(nombre = "Videojuegos", icono = IconosEstandar.VIDEOJUEGOS.icono),
        CategoriaEntity(nombre = "Viajes", icono = IconosEstandar.VIAJES.icono),
        CategoriaEntity(nombre = "Turismo", icono = IconosEstandar.TURISMO.icono),
        CategoriaEntity(nombre = "Deportes", icono = IconosEstandar.DEPORTES.icono),
        CategoriaEntity(nombre = "Gimnasio", icono = IconosEstandar.GIMNASIO.icono),

        // Ropa y accesorios
        CategoriaEntity(nombre = "Vestimenta", icono = IconosEstandar.VESTIMENTA.icono),
        CategoriaEntity(nombre = "Calzado", icono = IconosEstandar.CALZADO.icono),
        CategoriaEntity(nombre = "Accesorios", icono = IconosEstandar.ACCESORIOS.icono),

        // Hogar
        CategoriaEntity(nombre = "Electrodomésticos", icono = IconosEstandar.ELECTRODOMESTICOS.icono),
        CategoriaEntity(nombre = "Muebles", icono = IconosEstandar.MUEBLES.icono),
        CategoriaEntity(nombre = "Decoración", icono = IconosEstandar.DECORACION.icono),

        // Financieros
        CategoriaEntity(nombre = "Créditos", icono = IconosEstandar.CREDITOS.icono),
        CategoriaEntity(nombre = "Deudas", icono = IconosEstandar.DEUDAS.icono),
        CategoriaEntity(nombre = "Intereses bancarios", icono = IconosEstandar.INTERESES_BANCARIOS.icono),
        CategoriaEntity(nombre = "Comisiones", icono = IconosEstandar.COMISIONES.icono),

        // Mascotas
        CategoriaEntity(nombre = "Cuidado mascotas", icono = IconosEstandar.CUIDADO_MASCOTAS.icono),

        // Otros
        CategoriaEntity(nombre = "Otros", icono = IconosEstandar.OTROS.icono),
    )
}
