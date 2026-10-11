package com.yunta.app.domain.model

import java.time.LocalDate

enum class TipoMovimiento { INGRESO, GASTO }

enum class MedioPago { EFECTIVO, YAPE_PLIN, TARJETA }

/** Los montos se guardan en céntimos para no perder precisión con decimales. */
data class Movimiento(
    val id: Long = 0,
    val tipo: TipoMovimiento,
    val montoCentimos: Long,
    val categoria: Categoria,
    val fecha: LocalDate,
    val medioPago: MedioPago,
    val nota: String = "",
)
