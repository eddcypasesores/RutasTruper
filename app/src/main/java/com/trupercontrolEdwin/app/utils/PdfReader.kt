package com.trupercontrolEdwin.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object PdfReader {

    suspend fun leerPdf(context: Context, uri: Uri): String {
        return leerPdfConMLKit(context, uri)
    }

    /**
     * Convierte la primera página del PDF a imagen y usa ML Kit para extraer el texto.
     * Mejorado: Fondo blanco forzado para evitar problemas con PDFs transparentes.
     */
    private suspend fun leerPdfConMLKit(context: Context, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            try {
                val tempFile = File.createTempFile("temp_pdf", ".pdf", context.cacheDir)
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }

                val fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                val pdfRenderer = PdfRenderer(fileDescriptor)
                
                if (pdfRenderer.pageCount == 0) {
                    pdfRenderer.close()
                    fileDescriptor.close()
                    return@withContext ""
                }

                val page = pdfRenderer.openPage(0)
                // Aumentamos la escala para mejor reconocimiento (2f -> 3f)
                // Densidad más alta ayuda al OCR
                val scale = 3f
                val width = (page.width * scale).toInt()
                val height = (page.height * scale).toInt()
                
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                
                // Importante: Dibujar fondo blanco, ya que los PDFs a veces son transparentes
                // y al pasarlos a Bitmap negro sobre negro no se lee nada.
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(bitmap, 0f, 0f, null)

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                pdfRenderer.close()
                fileDescriptor.close()
                
                val image = InputImage.fromBitmap(bitmap, 0)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                
                val result = recognizer.process(image).await()
                
                bitmap.recycle()
                tempFile.delete()
                
                result.text
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }

    fun esAcuseCancelacion(texto: String): Boolean {
        return texto.contains("Acuse de solicitud de Cancelación", ignoreCase = true) ||
                texto.contains("Solicitud de cancelación recibida", ignoreCase = true)
    }

    fun esAvisoPago(texto: String): Boolean {
        return texto.contains("AVISO DE PAGO A PROVEEDOR NACIONAL", ignoreCase = true) ||
                texto.contains("TRUPER S.A. DE C.V.", ignoreCase = true)
    }

    fun extraerMontos(texto: String): List<Double> {
        val regex = Regex("\\$\\s*([0-9,]+\\.\\d{2})")
        return regex.findAll(texto).map {
            it.groupValues[1].replace(",", "").toDouble()
        }.toList()
    }
}
