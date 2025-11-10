package com.trupercontrolEdwin.app.utils

import android.content.Context
import android.net.Uri
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object PdfReader {

    /**
     * Lee un PDF desde un Uri y devuelve el texto.
     * Devuelve una cadena vacía si el PDF no se puede leer o causa un error.
     */
    fun leerPdf(context: Context, uri: Uri): String {
        return try {
            context.contentResolver.openInputStream(uri).use { input ->
                val doc = PDDocument.load(input)
                val stripper = PDFTextStripper()
                val texto = stripper.getText(doc)
                doc.close()
                texto
            }
        } catch (t: Throwable) {
            // Capturamos Throwable para incluir errores críticos como OutOfMemoryError.
            // Si algo sale mal (PDF corrupto, muy grande, etc.), devolvemos una cadena vacía.
            ""
        }
    }

    /**
     * Intenta detectar si es un acuse SAT de cancelación.
     */
    fun esAcuseCancelacion(texto: String): Boolean {
        return texto.contains("Acuse de solicitud de Cancelación", ignoreCase = true) ||
                texto.contains("Solicitud de cancelación recibida", ignoreCase = true)
    }

    /**
     * Intenta detectar si es aviso de pago TRUPER.
     */
    fun esAvisoPago(texto: String): Boolean {
        return texto.contains("AVISO DE PAGO A PROVEEDOR NACIONAL", ignoreCase = true) ||
                texto.contains("TRUPER S.A. DE C.V.", ignoreCase = true)
    }

    /**
     * Extrae totales $xx,xxx.xx
     */
    fun extraerMontos(texto: String): List<Double> {
        val regex = Regex("\\$\\s*([0-9,]+\\.\\d{2})")
        return regex.findAll(texto).map {
            it.groupValues[1].replace(",", "").toDouble()
        }.toList()
    }
}
