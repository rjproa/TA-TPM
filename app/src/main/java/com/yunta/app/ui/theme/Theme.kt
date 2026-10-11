package com.yunta.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Morado40,
    onPrimary = Color.White,
    primaryContainer = MoradoContenedor90,
    onPrimaryContainer = SobreMoradoContenedor10,
    secondary = Lavanda40,
    secondaryContainer = LavandaContenedor90,
    onSecondaryContainer = SobreLavandaContenedor10,
    tertiary = Ambar40,
    background = Fondo99,
    surface = Fondo99,
    surfaceContainer = Superficie95,
)

private val EsquemaOscuro = darkColorScheme(
    primary = Morado80,
    onPrimary = SobreMorado20,
    primaryContainer = MoradoContenedor30,
    onPrimaryContainer = MoradoContenedor90,
    secondary = Lavanda80,
    secondaryContainer = LavandaContenedor30,
    onSecondaryContainer = LavandaContenedor90,
    tertiary = Ambar80,
    background = Fondo6,
    surface = Fondo6,
    surfaceContainer = Superficie12,
)

/** Colores que Material no trae: ingreso y gasto. Se leen con YuntaTheme.colores. */
@Immutable
data class ColoresYunta(val ingreso: Color, val gasto: Color)

private val LocalColoresYunta = staticCompositionLocalOf {
    ColoresYunta(ingreso = VerdeIngreso, gasto = RojoGasto)
}

object YuntaTheme {
    val colores: ColoresYunta
        @Composable get() = LocalColoresYunta.current
}

@Composable
fun YuntaTheme(
    temaOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val coloresYunta = if (temaOscuro) {
        ColoresYunta(ingreso = VerdeIngresoOscuro, gasto = RojoGastoOscuro)
    } else {
        ColoresYunta(ingreso = VerdeIngreso, gasto = RojoGasto)
    }
    CompositionLocalProvider(LocalColoresYunta provides coloresYunta) {
        MaterialTheme(
            colorScheme = if (temaOscuro) EsquemaOscuro else EsquemaClaro,
            typography = Typography,
            content = content,
        )
    }
}
