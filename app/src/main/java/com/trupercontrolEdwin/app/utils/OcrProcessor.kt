package com.trupercontrolEdwin.app.utils

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.tasks.await

/**
 * Procesa una imagen (foto del listado o del texto de cambio)
 * y devuelve TODO el texto reconocido como String.
 */
object OcrProcessor {

    suspend fun procesarImagen(bitmap: Bitmap): String {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient()
        val result = recognizer.process(image).await()
        return result.text
    }

    /**
     * Intenta detectar patrones de folios en un texto OCRizado.
     * Por ejemplo: "Folio :54376" o "Folio 54376"
     */
    fun extraerFolios(texto: String): List<String> {
        val regex = Regex("(Folio\\s*[:#-]?\\s*)(\\d{4,6})", RegexOption.IGNORE_CASE)
        return regex.findAll(texto).map { it.groupValues[2] }.toList()
    }

    /**
     * Intenta detectar metros:
     * "36.00 m²" o "36 m2" o "Total 44.87 m²"
     */
    fun extraerMetros(texto: String): Double? {
        val regex = Regex("(\\d{1,4}\\.\\d{1,2}|\\d{1,4})\\s*(m²|m2)", RegexOption.IGNORE_CASE)
        val match = regex.find(texto) ?: return null
        return match.groupValues[1].replace(",", ".").toDouble()
    }
}
