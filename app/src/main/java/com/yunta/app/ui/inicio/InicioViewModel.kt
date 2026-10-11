package com.yunta.app.ui.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yunta.app.YuntaApplication
import com.yunta.app.data.repository.MovimientoRepository
import com.yunta.app.domain.model.Movimiento
import com.yunta.app.domain.model.TipoMovimiento
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth

data class InicioUiState(
    val ingresosCentimos: Long = 0,
    val gastosCentimos: Long = 0,
    val ultimosMovimientos: List<Movimiento> = emptyList(),
)

private const val MAXIMO_ULTIMOS = 5

class InicioViewModel(repository: MovimientoRepository) : ViewModel() {

    val uiState: StateFlow<InicioUiState> = repository.movimientos
        .map { movimientos -> resumir(movimientos, YearMonth.now()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InicioUiState())

    private fun resumir(movimientos: List<Movimiento>, mes: YearMonth): InicioUiState {
        val delMes = movimientos.filter { YearMonth.from(it.fecha) == mes }
        return InicioUiState(
            ingresosCentimos = delMes.total(TipoMovimiento.INGRESO),
            gastosCentimos = delMes.total(TipoMovimiento.GASTO),
            ultimosMovimientos = delMes
                .sortedWith(compareByDescending<Movimiento> { it.fecha }.thenByDescending { it.id })
                .take(MAXIMO_ULTIMOS),
        )
    }

    private fun List<Movimiento>.total(tipo: TipoMovimiento): Long =
        filter { it.tipo == tipo }.sumOf { it.montoCentimos }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as YuntaApplication
                InicioViewModel(app.container.movimientoRepository)
            }
        }
    }
}
