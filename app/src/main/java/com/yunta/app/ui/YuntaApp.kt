package com.yunta.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yunta.app.R
import com.yunta.app.ui.navigation.BarraInferior
import com.yunta.app.ui.navigation.DestinoPrincipal
import com.yunta.app.ui.navigation.Rutas
import com.yunta.app.ui.navigation.YuntaNavHost
import kotlinx.coroutines.launch

/** Estructura común a toda la app: barra superior, contenido y barra inferior. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YuntaApp() {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val destinoPrincipal = DestinoPrincipal.entries.find { it.ruta == rutaActual }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val mensajeGuardado = stringResource(R.string.formulario_guardado)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when (destinoPrincipal) {
                            null -> stringResource(R.string.formulario_titulo)
                            DestinoPrincipal.INICIO -> stringResource(R.string.app_name)
                            else -> stringResource(destinoPrincipal.titulo)
                        }
                    )
                },
                navigationIcon = {
                    if (destinoPrincipal == null && rutaActual != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.accion_atras),
                            )
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (destinoPrincipal != null) {
                BarraInferior(
                    destinoActual = destinoPrincipal,
                    onDestinoSeleccionado = { destino ->
                        navController.navigate(destino.ruta) {
                            // Evita apilar pantallas al cambiar de sección y conserva su estado.
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
        floatingActionButton = {
            if (destinoPrincipal == DestinoPrincipal.INICIO) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Rutas.NUEVO_MOVIMIENTO) },
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    text = { Text(stringResource(R.string.inicio_registrar)) },
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingInterno ->
        YuntaNavHost(
            navController = navController,
            onMovimientoGuardado = {
                navController.popBackStack()
                scope.launch { snackbarHostState.showSnackbar(mensajeGuardado) }
            },
            modifier = Modifier.padding(paddingInterno),
        )
    }
}
