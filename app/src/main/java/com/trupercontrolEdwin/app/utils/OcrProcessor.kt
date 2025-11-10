package com.trupercontrolEdwin.app.utils

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Procesa una imagen (foto del listado o del texto de cambio)
 * y devuelve TODO el texto reconocido como String.
 */
object OcrProcessor {

    suspend fun procesarImagen(bitmap: Bitmap): String = suspendCoroutine { continuation ->
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { result ->
                continuation.resume(result.text)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    /**
     * Intenta detectar patrones de folios en un texto OCRizado.
     * Combina dos estrategias: busca el patrón "Folio: numero" y también números aislados.
     */
    fun extraerFolios(texto: String): List<String> {
        val regexConPrefijo = Regex("""(Folio|Foli|Folio:)\s*[:#-]?\s*(\d{5,6})""", RegexOption.IGNORE_CASE)
        val foliosConPrefijo = regexConPrefijo.findAll(texto).map { it.groupValues[2] }.toList()

        if (foliosConPrefijo.isNotEmpty()) {
            return foliosConPrefijo
        }

        val regexNumerosAislados = Regex("""\b(\d{5,6})\b""", RegexOption.IGNORE_CASE)
        return regexNumerosAislados.findAll(texto).map { it.groupValues[1] }.toList()
    }

    /**
     * Intenta detectar metros:
     * "36.00 m²" o "36 m2" o "Total 44.87 m²"
     */
    fun extraerMetros(texto: String): Double? {
        val regex = Regex("""(\d{1,4}[,.]?\d{1,2}|\d{1,4})\s*(m²|m2)""", RegexOption.IGNORE_CASE)
        val match = regex.find(texto) ?: return null
        return match.groupValues[1].replace(",", ".").toDoubleOrNull()
    }
}
