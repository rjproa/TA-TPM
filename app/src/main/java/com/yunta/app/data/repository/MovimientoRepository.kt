package com.yunta.app.data.repository

import com.yunta.app.data.DatosDeEjemplo
import com.yunta.app.domain.model.Categoria
import com.yunta.app.domain.model.Movimiento
import com.yunta.app.domain.model.TipoMovimiento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Los ViewModel dependen de esta interfaz, no de dónde se guardan los datos. */
interface MovimientoRepository {
    val movimientos: Flow<List<Movimiento>>
    fun categorias(tipo: TipoMovimiento): List<Categoria>
    suspend fun agregar(movimiento: Movimiento)
}

/** Implementación provisional de la fase 0: los datos se pierden al cerrar la app. */
class MovimientoRepositoryEnMemoria : MovimientoRepository {

    private val _movimientos = MutableStateFlow(DatosDeEjemplo.movimientos())
    override val movimientos: Flow<List<Movimiento>> = _movimientos.asStateFlow()

    override fun categorias(tipo: TipoMovimiento): List<Categoria> =
        DatosDeEjemplo.categorias.filter { it.tipo == tipo }

    override suspend fun agregar(movimiento: Movimiento) {
        _movimientos.update { actuales ->
            val siguienteId = (actuales.maxOfOrNull { it.id } ?: 0) + 1
            actuales + movimiento.copy(id = siguienteId)
        }
    }
}
