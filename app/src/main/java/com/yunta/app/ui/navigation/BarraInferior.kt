package com.yunta.app.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun BarraInferior(
    destinoActual: DestinoPrincipal,
    onDestinoSeleccionado: (DestinoPrincipal) -> Unit,
) {
    NavigationBar {
        DestinoPrincipal.entries.forEach { destino ->
            NavigationBarItem(
                selected = destino == destinoActual,
                onClick = { onDestinoSeleccionado(destino) },
                icon = { Icon(destino.icono, contentDescription = null) },
                label = { Text(stringResource(destino.titulo)) },
            )
        }
    }
}
