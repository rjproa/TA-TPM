package com.yunta.app.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yunta.app.R
import com.yunta.app.ui.components.PantallaEnConstruccion
import com.yunta.app.ui.navigation.DestinoPrincipal

@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    PantallaEnConstruccion(
        icono = DestinoPrincipal.DASHBOARD.icono,
        descripcion = stringResource(R.string.dashboard_descripcion),
        modifier = modifier,
    )
}
