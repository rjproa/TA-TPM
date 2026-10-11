package com.yunta.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yunta.app.R
import com.yunta.app.domain.aSoles
import com.yunta.app.ui.theme.YuntaTheme

@Composable
fun TarjetaResumen(
    ingresosCentimos: Long,
    gastosCentimos: Long,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                stringResource(R.string.inicio_disponible),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                (ingresosCentimos - gastosCentimos).aSoles(),
                style = MaterialTheme.typography.displaySmall,
            )
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth()) {
                Total(
                    etiqueta = stringResource(R.string.inicio_ingresos),
                    monto = ingresosCentimos.aSoles(),
                    color = YuntaTheme.colores.ingreso,
                    modifier = Modifier.weight(1f),
                )
                Total(
                    etiqueta = stringResource(R.string.inicio_gastos),
                    monto = gastosCentimos.aSoles(),
                    color = YuntaTheme.colores.gasto,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun Total(etiqueta: String, monto: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(etiqueta, style = MaterialTheme.typography.labelMedium)
        Text(monto, style = MaterialTheme.typography.titleMedium, color = color)
    }
}
