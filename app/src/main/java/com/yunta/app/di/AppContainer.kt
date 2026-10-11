package com.yunta.app.di

import com.yunta.app.data.repository.MovimientoRepository
import com.yunta.app.data.repository.MovimientoRepositoryEnMemoria

/** Crea una sola vez los objetos que comparten las pantallas. */
class AppContainer {
    val movimientoRepository: MovimientoRepository = MovimientoRepositoryEnMemoria()
}
