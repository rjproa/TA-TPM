package com.yunta.app.ui.movimientos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yunta.app.R
import com.yunta.app.domain.model.MedioPago
import com.yunta.app.domain.model.TipoMovimiento
import com.yunta.app.ui.components.BotonPrimario
import com.yunta.app.ui.components.CampoMonto
import com.yunta.app.ui.components.etiqueta

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FormularioMovimientoScreen(
    onGuardado: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FormularioMovimientoViewModel = viewModel(factory = FormularioMovimientoViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.guardado) {
        if (uiState.guardado) onGuardado()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        val tipos = listOf(
            TipoMovimiento.GASTO to R.string.formulario_tipo_gasto,
            TipoMovimiento.INGRESO to R.string.formulario_tipo_ingreso,
        )
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            tipos.forEachIndexed { indice, (tipo, etiqueta) ->
                SegmentedButton(
                    selected = uiState.tipo == tipo,
                    onClick = { viewModel.onTipoCambiado(tipo) },
                    shape = SegmentedButtonDefaults.itemShape(indice, tipos.size),
                ) {
                    Text(stringResource(etiqueta))
                }
            }
        }

        CampoMonto(
            valor = uiState.montoTexto,
            onValorCambiado = viewModel::onMontoCambiado,
            error = uiState.errorMonto?.let { stringResource(it) },
        )

        Column {
            Text(
                stringResource(R.string.formulario_categoria),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.categorias.forEach { categoria ->
                    FilterChip(
                        selected = categoria == uiState.categoriaSeleccionada,
                        onClick = { viewModel.onCategoriaSeleccionada(categoria) },
                        label = { Text(categoria.nombre) },
                    )
                }
            }
            uiState.errorCategoria?.let { error ->
                Text(
                    stringResource(error),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        Column {
            Text(
                stringResource(R.string.formulario_medio_pago),
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MedioPago.entries.forEach { medio ->
                    FilterChip(
                        selected = medio == uiState.medioPago,
                        onClick = { viewModel.onMedioPagoCambiado(medio) },
                        label = { Text(stringResource(medio.etiqueta())) },
                    )
                }
            }
        }

        OutlinedTextField(
            value = uiState.nota,
            onValueChange = viewModel::onNotaCambiada,
            label = { Text(stringResource(R.string.formulario_nota)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        BotonPrimario(
            texto = stringResource(R.string.formulario_guardar),
            onClick = viewModel::guardar,
        )
    }
}
