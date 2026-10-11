package com.yunta.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DineroTest {

    @Test
    fun parsearMonto_aceptaEnterosYDecimales() {
        assertEquals(3000L, parsearMonto("30"))
        assertEquals(3050L, parsearMonto("30.5"))
        assertEquals(3005L, parsearMonto("30.05"))
        assertEquals(3050L, parsearMonto("30,50"))
        assertEquals(0L, parsearMonto("0"))
        assertEquals(1250L, parsearMonto(" 12.5 "))
    }

    @Test
    fun parsearMonto_rechazaTextoInvalido() {
        assertNull(parsearMonto(""))
        assertNull(parsearMonto("abc"))
        assertNull(parsearMonto("-5"))
        assertNull(parsearMonto("1.234"))
        assertNull(parsearMonto("1.2.3"))
        assertNull(parsearMonto("."))
        assertNull(parsearMonto("12."))
    }

    @Test
    fun aSoles_formateaConMilesYDosDecimales() {
        assertEquals("S/ 0.00", 0L.aSoles())
        assertEquals("S/ 30.00", 3000L.aSoles())
        assertEquals("S/ 1,234.50", 123450L.aSoles())
        assertEquals("-S/ 12.05", (-1205L).aSoles())
    }
}
