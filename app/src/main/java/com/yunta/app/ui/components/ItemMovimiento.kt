package com.yunta.app.ui.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yunta.app.R
import com.yunta.app.domain.aSoles
import com.yunta.app.domain.model.MedioPago
import com.yunta.app.domain.model.Movimiento
import com.yunta.app.domain.model.TipoMovimiento
import com.yunta.app.ui.theme.YuntaTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FORMATO_FECHA = DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("es-PE"))

@Composable
fun ItemMovimiento(movimiento: Movimiento, modifier: Modifier = Modifier) {
    val esIngreso = movimiento.tipo == TipoMovimiento.INGRESO
    val detalle = listOf(
        movimiento.fecha.format(FORMATO_FECHA),
        stringResource(movimiento.medioPago.etiqueta()),
        movimiento.nota,
    ).filter { it.isNotBlank() }.joinToString(" · ")

    ListItem(
        headlineContent = { Text(movimiento.categoria.nombre) },
        supportingContent = { Text(detalle) },
        trailingContent = {
            Text(
                text = (if (esIngreso) "+ " else "- ") + movimiento.montoCentimos.aSoles(),
                style = MaterialTheme.typography.titleMedium,
                color = if (esIngreso) YuntaTheme.colores.ingreso else YuntaTheme.colores.gasto,
            )
        },
        modifier = modifier,
    )
}

fun MedioPago.etiqueta(): Int = when (this) {
    MedioPago.EFECTIVO -> R.string.medio_efectivo
    MedioPago.YAPE_PLIN -> R.string.medio_yape_plin
    MedioPago.TARJETA -> R.string.medio_tarjeta
}
