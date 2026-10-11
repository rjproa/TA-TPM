package com.yunta.app.domain

import java.util.Locale
import kotlin.math.abs

private val FORMATO_MONTO = Regex("""\d{1,7}([.,]\d{1,2})?""")

/** Convierte céntimos a texto en soles: 123450 -> "S/ 1,234.50". */
fun Long.aSoles(): String {
    val signo = if (this < 0) "-" else ""
    val absoluto = abs(this)
    return String.format(Locale.US, "%sS/ %,d.%02d", signo, absoluto / 100, absoluto % 100)
}

/**
 * Convierte el texto de un campo de monto a céntimos.
 * Devuelve null si no es un número positivo con dos decimales como máximo.
 */
fun parsearMonto(texto: String): Long? {
    val limpio = texto.trim()
    if (!FORMATO_MONTO.matches(limpio)) return null
    val partes = limpio.replace(',', '.').split('.')
    val soles = partes[0].toLong()
    val centimos = partes.getOrNull(1)?.padEnd(2, '0')?.toLong() ?: 0
    return soles * 100 + centimos
}
