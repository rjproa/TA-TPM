package com.yunta.app.domain.model

/** Grupos de la regla 50/30/20. */
enum class GrupoPresupuesto { NECESIDAD, GUSTO, AHORRO }

data class Categoria(
    val id: Long,
    val nombre: String,
    val tipo: TipoMovimiento,
    /** Solo las categorías de gasto pertenecen a un grupo del presupuesto. */
    val grupo: GrupoPresupuesto? = null,
)
