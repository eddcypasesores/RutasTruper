package com.trupercontrolEdwin.app.utils

import org.junit.Assert.*
import org.junit.Test

class FolioParserTest {

    val textoProblema = """
Datos del cliente
 Número de cliente:557672 Sucursal: Fecha: Zona: SDF 1237 12/08/2025
 Razón social del cliente:
 Nombre de la ferretería a rotular:
 Domicilio de la ferreteía a rotular:
 Población, Ciudad:
 Contacto de la ferretería:
 Valencia Goriba Norma Angelica
 Tlapalería Magaly
 Av.Morelos No 9
 Norma Valencia Goriba
 Oxtotipac, Otumba Estado: Estado de México
 Teléfono: 55 40259973
 Reporte de Medidas
 Tipo de rotulación: Si
 3.- Fachada Derecha
 Espacios
 Rotulación parcial: Aplica alguna promoción: TRUPER No
 Cantidad Ancho Alto Total
 2.- Fachada Izquierda
 5.- Marquesina
 4.- Muros
 1.- Fachada principal
 9.- Otros (Restar)
 8.- Ventanas (Restar)
 7.- Mostradores
 6.- Cortina
 10.- Instalación de ceramica:
 1 3.00 5.50 16.50
 0
 0
 0
 3
 1
 0
 0
 0
 0.00
 3.00
 0.00
 0.00
 0.00
 0.00
 2.20
 X
 5.50
 0.00
 0.00
 0.00
 2.46
 0.00
 0.00
 0.00
 0.00
 16.50
 0.00
 0.00
 0.00
 16.24
 0.00
 0.00 0.00
 m
 m
 m
 m
 m
 m
 m
 m
 m
 m2
 m2
 m2
 m
 m
 m
 m
 m
 m
 m
 m
 m m2
 m2
 m2
 m2
 m2
 m2
 0.0
 Total de metros cuadrados a rotular: 49.00m2
 Nombre del asesor que reporta las medidas: Joaquín Durán Jiménez
 Teléfono celular del asesor de ventas: (Incluye LD): 55 84903785
 Datos de facturación
 Precio por m2 $
 Instalación de cerámica $
 180.0X total de M2 sin promoción $ 0.00
 0.0
 Total a facturar  $ 5,292.00
 Solicitud de Rotulación
 Folio: 53949
 Nota: El importe de facturación y las promociones no aplican en interiores.
 49.00 Metros con valor con promoción: m2
 Metros con valor sin promoción: 0.00 m2
 Precio por m2 $108.0X total de M2 con promocion $ 5,292.00
 Solicitud registrada y aceptada por el asesor: (D05SDFSD281237)D05SDFSD281237, fecha: 12/Ago/2025
 Solicitud aceptada por el gerente: (gbautistag) Griselda Bautista Garcia, fecha: 12/Ago/2025
 Se agrega a la solicitud la fecha tentativa: 21/Oct/2025, el día: 12/Ago/2025, por el usuario: (emolinag
    """.trimIndent()

    @Test
    fun testParseSolicitud_TextoProblema() {
        val resultado = FolioParser.parseSolicitud(textoProblema)
        assertNotNull("El resultado no debería ser null", resultado)
        assertEquals("53949", resultado?.folio)
        assertEquals("Tlapalería Magaly", resultado?.nombreNegocio)
        assertEquals("Av.Morelos No 9", resultado?.direccion)
        assertEquals(49.00, resultado?.metrosReportados)
    }

    @Test
    fun testParseSolicitud_EjemploImagen() {
        val texto = """
            TRUPER
            Solicitud de Rotulación
            Folio: 53949    MS
            Datos del cliente
            Número de cliente: 557672 Sucursal:SDF Zona:1237 Fecha:12/08/2025
            Razón social del cliente: Valencia Goriba Norma Angelica
            Nombre de la ferretería a rotular: Tlapalería Magaly
            Población, Ciudad: Oxtotipac, Otumba Estado: Estado de México
            Domicilio de la ferretería a rotular: Av.Morelos No 9
            Contacto de la ferretería:Norma Valencia Goriba Teléfono: 55 40259973
            Entre Calle: Calvario
            Y Calle: Francisco y madero
            Reporte de Medidas
            Tipo de rotulación: TRUPER Aplica alguna promoción: Si Rotulación parcial: No
            Espacios Cantidad Alto X Ancho Total
            1.- Fachada principal 1 3.00 m 5.50 m 16.50 m2
            Total de metros cuadrados a rotular: 49.00 m2
        """.trimIndent()

        val resultado = FolioParser.parseSolicitud(texto)
        assertNotNull("El resultado no debería ser null", resultado)
        assertEquals("53949", resultado?.folio)
        assertEquals("Tlapalería Magaly", resultado?.nombreNegocio)
        assertEquals("Av.Morelos No 9", resultado?.direccion)
        assertEquals(49.00, resultado?.metrosReportados)
    }
}
