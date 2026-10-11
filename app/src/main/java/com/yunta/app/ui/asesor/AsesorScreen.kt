package com.yunta.app.ui.asesor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yunta.app.R
import com.yunta.app.ui.components.PantallaEnConstruccion
import com.yunta.app.ui.navigation.DestinoPrincipal

@Composable
fun AsesorScreen(modifier: Modifier = Modifier) {
    PantallaEnConstruccion(
        icono = DestinoPrincipal.ASESOR.icono,
        descripcion = stringResource(R.string.asesor_descripcion),
        modifier = modifier,
    )
}
