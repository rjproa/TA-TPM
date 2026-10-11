package com.yunta.app.ui.inicio

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yunta.app.R
import com.yunta.app.ui.components.ItemMovimiento
import com.yunta.app.ui.components.TarjetaResumen

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel = viewModel(factory = InicioViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        // El espacio inferior evita que el botón flotante tape el último movimiento.
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            Text(
                stringResource(R.string.inicio_saludo),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        item {
            TarjetaResumen(
                ingresosCentimos = uiState.ingresosCentimos,
                gastosCentimos = uiState.gastosCentimos,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        item {
            Text(
                stringResource(R.string.inicio_ultimos_movimientos),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
            )
        }
        if (uiState.ultimosMovimientos.isEmpty()) {
            item {
                Text(
                    stringResource(R.string.inicio_sin_movimientos),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
        items(uiState.ultimosMovimientos, key = { it.id }) { movimiento ->
            ItemMovimiento(movimiento)
            HorizontalDivider()
        }
    }
}
