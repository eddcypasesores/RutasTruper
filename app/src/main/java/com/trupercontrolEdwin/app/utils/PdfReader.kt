package com.trupercontrolEdwin.app.utils

import android.content.Context
import android.net.Uri
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object PdfReader {

    /**
     * Lee un PDF desde un Uri (PDF de Truper, acuse de cancelación, aviso de pago)
     * y devuelve TODO el texto.
     */
    fun leerPdf(context: Context, uri: Uri): String {
        context.contentResolver.openInputStream(uri).use { input ->
            val doc = PDDocument.load(input)
            val stripper = PDFTextStripper()
            val texto = stripper.getText(doc)
            doc.close()
            return texto
        }
    }

    /**
     * Intenta detectar si es un acuse SAT de cancelación (como el que subiste).
     */
    fun esAcuseCancelacion(texto: String): Boolean {
        return texto.contains("Acuse de solicitud de Cancelación", ignoreCase = true) ||
                texto.contains("Solicitud de cancelación recibida", ignoreCase = true)
    }

    /**
     * Intenta detectar si es aviso de pago TRUPER (como NP0000700139.PDF)
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
