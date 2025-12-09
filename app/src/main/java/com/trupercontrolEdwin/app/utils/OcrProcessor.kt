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
     * Detecta folios en un texto OCRizado.
     * Mejorado para capturar listas verticales de números.
     */
    fun extraerFolios(texto: String): List<String> {
        val encontrados = mutableSetOf<String>()

        // 1. Estrategia explícita: "Folio: 12345"
        // Útil si aparecen con etiqueta
        val regexConPrefijo = Regex("""(?:Folio|Foli|Folio:)[\s.:#-]*(\d{5,6})""", RegexOption.IGNORE_CASE)
        regexConPrefijo.findAll(texto).forEach { 
            encontrados.add(it.groupValues[1]) 
        }

        // 2. Estrategia de lista: Números de 5 o 6 dígitos aislados
        // Se usa \b para asegurar que son números completos, pero se permite que estén rodeados de saltos de línea
        // En OCR de listas, a veces los números vienen pegados o con ruido, intentamos limpiar primero
        
        // Buscamos cualquier secuencia de 5 o 6 dígitos
        val regexNumeros = Regex("""(?<!\d)(\d{5,6})(?!\d)""")
        regexNumeros.findAll(texto).forEach { 
            encontrados.add(it.groupValues[1]) 
        }

        return encontrados.toList().sorted()
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
