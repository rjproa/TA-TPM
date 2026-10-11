package com.yunta.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yunta.app.ui.ajustes.AjustesScreen
import com.yunta.app.ui.asesor.AsesorScreen
import com.yunta.app.ui.dashboard.DashboardScreen
import com.yunta.app.ui.historial.HistorialScreen
import com.yunta.app.ui.inicio.InicioScreen
import com.yunta.app.ui.movimientos.FormularioMovimientoScreen

@Composable
fun YuntaNavHost(
    navController: NavHostController,
    onMovimientoGuardado: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = DestinoPrincipal.INICIO.ruta,
        modifier = modifier,
    ) {
        composable(DestinoPrincipal.INICIO.ruta) { InicioScreen() }
        composable(DestinoPrincipal.HISTORIAL.ruta) { HistorialScreen() }
        composable(DestinoPrincipal.DASHBOARD.ruta) { DashboardScreen() }
        composable(DestinoPrincipal.ASESOR.ruta) { AsesorScreen() }
        composable(DestinoPrincipal.AJUSTES.ruta) { AjustesScreen() }
        composable(Rutas.NUEVO_MOVIMIENTO) {
            FormularioMovimientoScreen(onGuardado = onMovimientoGuardado)
        }
    }
}
