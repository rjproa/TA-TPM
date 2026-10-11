package com.yunta.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.yunta.app.R

/** Secciones que aparecen en la barra inferior. */
enum class DestinoPrincipal(
    val ruta: String,
    @param:StringRes val titulo: Int,
    val icono: ImageVector,
) {
    INICIO("inicio", R.string.nav_inicio, Icons.Filled.Home),
    HISTORIAL("historial", R.string.nav_historial, Icons.AutoMirrored.Filled.ReceiptLong),
    DASHBOARD("dashboard", R.string.nav_dashboard, Icons.Filled.BarChart),
    ASESOR("asesor", R.string.nav_asesor, Icons.AutoMirrored.Filled.Chat),
    AJUSTES("ajustes", R.string.nav_ajustes, Icons.Filled.Settings),
}

/** Pantallas a las que se llega desde otra pantalla, sin barra inferior. */
object Rutas {
    const val NUEVO_MOVIMIENTO = "movimientos/nuevo"
}
