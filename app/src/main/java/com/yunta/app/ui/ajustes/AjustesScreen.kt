package com.yunta.app.ui.ajustes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yunta.app.R
import com.yunta.app.ui.components.PantallaEnConstruccion
import com.yunta.app.ui.navigation.DestinoPrincipal

@Composable
fun AjustesScreen(modifier: Modifier = Modifier) {
    PantallaEnConstruccion(
        icono = DestinoPrincipal.AJUSTES.icono,
        descripcion = stringResource(R.string.ajustes_descripcion),
        modifier = modifier,
    )
}
