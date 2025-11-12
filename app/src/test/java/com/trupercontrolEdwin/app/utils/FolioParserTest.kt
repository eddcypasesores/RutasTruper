package com.trupercontrolEdwin.app.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

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
        val solicitud = resultado!!
        assertEquals("53949", solicitud.folio)
        assertEquals("Tlapalería Magaly", solicitud.nombreNegocio)
        assertEquals("Av. Morelos No 9", solicitud.direccion)
        assertEquals(49.0, solicitud.metrosReportados!!, 0.0001)
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
        val solicitud = resultado!!
        assertEquals("12345", solicitud.folio)
        assertEquals("Ferretería Ejemplo", solicitud.nombreNegocio)
        assertEquals("Calle 123 Colonia Centro", solicitud.direccion)
        assertEquals(75.5, solicitud.metrosReportados!!, 0.0001)
    }
}