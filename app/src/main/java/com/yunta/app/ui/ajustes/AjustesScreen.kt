package com.yunta.app.ui.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yunta.app.BuildConfig
import com.yunta.app.R
import com.yunta.app.ui.components.PantallaEnConstruccion
import com.yunta.app.ui.navigation.DestinoPrincipal

@Composable
fun AjustesScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        PantallaEnConstruccion(
            icono = DestinoPrincipal.AJUSTES.icono,
            descripcion = stringResource(R.string.ajustes_descripcion),
            modifier = Modifier.weight(1f),
        )
        // Permite saber qué compilación está instalada en el teléfono.
        Text(
            stringResource(R.string.ajustes_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            stringResource(
                R.string.ajustes_compilacion,
                BuildConfig.BUILD_RAMA,
                BuildConfig.BUILD_COMMIT.take(7),
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}
