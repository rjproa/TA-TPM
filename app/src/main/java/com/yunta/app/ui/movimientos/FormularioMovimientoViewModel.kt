package com.yunta.app.ui.movimientos

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yunta.app.R
import com.yunta.app.YuntaApplication
import com.yunta.app.data.repository.MovimientoRepository
import com.yunta.app.domain.model.Categoria
import com.yunta.app.domain.model.MedioPago
import com.yunta.app.domain.model.Movimiento
import com.yunta.app.domain.model.TipoMovimiento
import com.yunta.app.domain.parsearMonto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class FormularioUiState(
    val tipo: TipoMovimiento = TipoMovimiento.GASTO,
    val montoTexto: String = "",
    val categorias: List<Categoria> = emptyList(),
    val categoriaSeleccionada: Categoria? = null,
    val medioPago: MedioPago = MedioPago.EFECTIVO,
    val nota: String = "",
    @param:StringRes val errorMonto: Int? = null,
    @param:StringRes val errorCategoria: Int? = null,
    val guardado: Boolean = false,
)

class FormularioMovimientoViewModel(
    private val repository: MovimientoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FormularioUiState(categorias = repository.categorias(TipoMovimiento.GASTO))
    )
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun onTipoCambiado(tipo: TipoMovimiento) {
        if (tipo == _uiState.value.tipo) return
        // Las categorías dependen del tipo, así que la elegida deja de ser válida.
        _uiState.update {
            it.copy(
                tipo = tipo,
                categorias = repository.categorias(tipo),
                categoriaSeleccionada = null,
                errorCategoria = null,
            )
        }
    }

    fun onMontoCambiado(texto: String) {
        _uiState.update { it.copy(montoTexto = texto, errorMonto = null) }
    }

    fun onCategoriaSeleccionada(categoria: Categoria) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria, errorCategoria = null) }
    }

    fun onMedioPagoCambiado(medioPago: MedioPago) {
        _uiState.update { it.copy(medioPago = medioPago) }
    }

    fun onNotaCambiada(nota: String) {
        _uiState.update { it.copy(nota = nota) }
    }

    fun guardar() {
        val estado = _uiState.value
        val monto = parsearMonto(estado.montoTexto)
        val errorMonto = when {
            estado.montoTexto.isBlank() -> R.string.error_monto_vacio
            monto == null -> R.string.error_monto_invalido
            monto == 0L -> R.string.error_monto_cero
            else -> null
        }
        val errorCategoria =
            if (estado.categoriaSeleccionada == null) R.string.error_categoria else null

        if (monto == null || errorMonto != null || estado.categoriaSeleccionada == null) {
            _uiState.update { it.copy(errorMonto = errorMonto, errorCategoria = errorCategoria) }
            return
        }

        viewModelScope.launch {
            repository.agregar(
                Movimiento(
                    tipo = estado.tipo,
                    montoCentimos = monto,
                    categoria = estado.categoriaSeleccionada,
                    fecha = LocalDate.now(),
                    medioPago = estado.medioPago,
                    nota = estado.nota.trim(),
                )
            )
            _uiState.update { it.copy(guardado = true) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as YuntaApplication
                FormularioMovimientoViewModel(app.container.movimientoRepository)
            }
        }
    }
}
