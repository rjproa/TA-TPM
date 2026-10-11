package com.yunta.app.data

import com.yunta.app.domain.model.Categoria
import com.yunta.app.domain.model.GrupoPresupuesto
import com.yunta.app.domain.model.MedioPago
import com.yunta.app.domain.model.Movimiento
import com.yunta.app.domain.model.TipoMovimiento
import java.time.LocalDate

/** Datos provisionales de la fase 0. En la fase 1 los reemplaza la base de datos. */
object DatosDeEjemplo {

    val categorias = listOf(
        Categoria(1, "Sueldo", TipoMovimiento.INGRESO),
        Categoria(2, "Otros ingresos", TipoMovimiento.INGRESO),
        Categoria(3, "Comida", TipoMovimiento.GASTO, GrupoPresupuesto.NECESIDAD),
        Categoria(4, "Transporte", TipoMovimiento.GASTO, GrupoPresupuesto.NECESIDAD),
        Categoria(5, "Alquiler", TipoMovimiento.GASTO, GrupoPresupuesto.NECESIDAD),
        Categoria(6, "Servicios", TipoMovimiento.GASTO, GrupoPresupuesto.NECESIDAD),
        Categoria(7, "Ropa", TipoMovimiento.GASTO, GrupoPresupuesto.GUSTO),
        Categoria(8, "Ocio", TipoMovimiento.GASTO, GrupoPresupuesto.GUSTO),
        Categoria(9, "Ahorro", TipoMovimiento.GASTO, GrupoPresupuesto.AHORRO),
    )

    fun movimientos(hoy: LocalDate = LocalDate.now()): List<Movimiento> = listOf(
        Movimiento(1, TipoMovimiento.INGRESO, 200_000, categorias[0], hoy.withDayOfMonth(1), MedioPago.TARJETA),
        Movimiento(2, TipoMovimiento.GASTO, 70_000, categorias[4], hoy.withDayOfMonth(1), MedioPago.YAPE_PLIN),
        Movimiento(3, TipoMovimiento.GASTO, 3_000, categorias[2], hoy, MedioPago.EFECTIVO, "Menú"),
        Movimiento(4, TipoMovimiento.GASTO, 450, categorias[3], hoy, MedioPago.EFECTIVO),
        Movimiento(5, TipoMovimiento.GASTO, 6_000, categorias[6], hoy, MedioPago.TARJETA, "Polo"),
    )
}
