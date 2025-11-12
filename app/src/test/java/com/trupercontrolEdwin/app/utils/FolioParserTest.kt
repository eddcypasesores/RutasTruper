package com.trupercontrolEdwin.app.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FolioParserTest {

    @Test
    fun parseSolicitud_extractsCamposPrincipales() {
        val texto = """
            Folio: 53949
            Nombre de la ferretería a rotular: Tlapalería Magaly
            Domicilio de la ferretería a rotular: Av. Morelos No 9
            Total de metros cuadrados a rotular: 49.00 m2
        """.trimIndent()

        val resultado = FolioParser.parseSolicitud(texto)

        assertNotNull(resultado)
        assertEquals("53949", resultado.folio)
        assertEquals("Tlapalería Magaly", resultado.nombreNegocio)
        assertEquals("Av. Morelos No 9", resultado.direccion)
        assertEquals(49.0, resultado.metrosReportados, 0.0001)
    }

    @Test
    fun parseSolicitud_identificaEtiquetasConEspaciadoEntreLetras() {
        val texto = """
            Folio: 12345
            N o m b r e   d e   l a   f e r r e t e r í a   a   r o t u l a r :
            Ferretería Ejemplo
            D o m i c i l i o   d e   l a   f e r r e t e r í a   a   r o t u l a r :
            Calle 123 Colonia Centro
            T o t a l   d e   m e t r o s   c u a d r a d o s   a   r o t u l a r :
            75.5 m2
        """.trimIndent()

        val resultado = FolioParser.parseSolicitud(texto)

        assertNotNull(resultado)
        assertEquals("12345", resultado.folio)
        assertEquals("Ferretería Ejemplo", resultado.nombreNegocio)
        assertEquals("Calle 123 Colonia Centro", resultado.direccion)
        assertEquals(75.5, resultado.metrosReportados, 0.0001)
    }
}
