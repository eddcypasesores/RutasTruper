package com.trupercontrolEdwin.app.utils

import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.data.entities.Ruta
import java.text.Normalizer
import kotlin.math.abs

object FolioParser {

    data class SolicitudData(
        val folio: String,
        val nombreNegocio: String? = null,
        val direccion: String? = null,
        val tipoFachada: String? = null,
        val metrosReportados: Double? = null,
        val extraInfo: String? = null 
    )

    fun parseSolicitud(texto: String): SolicitudData? {
        val folioRegex = Regex("""(?im)Folio\s*(?:No\.?|[:#-·])?\s*([0-9\s]{4,12})""")
        val folioMatch = folioRegex.find(texto)
        val rawFolio = folioMatch?.groupValues?.get(1)
        
        val folio = rawFolio?.filter { it.isDigit() }?.takeIf { it.length >= 4 } ?: return null

        val textoNormalizado = texto.normalizeForComparison()

        val nombreNegocio = findNextTextValue(
            texto, 
            textoNormalizado,
            listOf("Nombre de la ferretería a rotular", "Nombre del negocio", "Razón social del cliente"),
            maxLength = 100
        )

        val poblacionCiudad = findNextTextValue(
            texto,
            textoNormalizado,
            listOf("Población", "Ciudad"),
            maxLength = 100
        )

        val estado = findNextTextValue(
            texto,
            textoNormalizado,
            listOf("Estado"),
            maxLength = 50
        )

        val direccion = listOfNotNull(poblacionCiudad, estado)
            .filter { it.isNotBlank() }
            .joinToString(", ")
            .takeIf { it.isNotEmpty() }

        val metrosRegex = Regex("""(?i)(Total\s+de\s+metros\s+cuadrados\s+a\s+rotular|Metros\s+con\s+valor\s+con\s+promocion)\s*[:#-\.\s]*([\d\s,]+\.\d{2})\s*(m2|m²)?""")
        val metrosMatch = metrosRegex.find(texto)
        val metrosReportados = metrosMatch?.groupValues?.get(2)?.normalizeDouble()

        val tipoFachada = findNextTextValue(
            texto,
            textoNormalizado,
            listOf("Tipo de rotulación", "Tipo de fachada"),
            maxLength = 50
        )

        return SolicitudData(
            folio = folio,
            nombreNegocio = nombreNegocio,
            direccion = direccion,
            tipoFachada = tipoFachada,
            metrosReportados = metrosReportados
        )
    }

    private fun findNextTextValue(
        textoOriginal: String,
        textoNormalizado: String,
        etiquetas: List<String>,
        maxLength: Int
    ): String? {
        val etiquetasNormalizadas = etiquetas.map { it.normalizeForComparison() }
        val matchedLabelNorm = etiquetasNormalizadas.firstOrNull { textoNormalizado.contains(it) } ?: return null
        
        val etiquetaOriginal = etiquetas.first { it.normalizeForComparison() == matchedLabelNorm }
        
        val words = etiquetaOriginal.split("\\s+".toRegex())
        val regexPattern = words.joinToString(separator = """\s+""", prefix = """(?i)""") { Regex.escape(it) }
        
        val matchResult = Regex(regexPattern).find(textoOriginal) ?: return null
        
        val startIndex = matchResult.range.last + 1
        if (startIndex >= textoOriginal.length) return null
        
        var rawValue = textoOriginal.substring(startIndex)
        
        rawValue = rawValue.replaceFirst(Regex("""^[\s:.\-·]+"""), "")
        
        if (rawValue.isEmpty()) return null

        val limit = minOf(rawValue.length, maxLength)
        var candidate = rawValue.substring(0, limit)

        val knownLabels = listOf(
            "Folio", "Nombre de la ferretería", "Nombre del negocio", "Razón social", 
            "Domicilio", "Población", "Ciudad", "Estado", "Teléfono", "Contacto", 
            "Correo", "Total de metros", "Metros con valor", "Tipo de rotulación", 
            "Fecha", "Zona", "Entre calles", "Colonia", "CP", "C.P."
        )
        
        var endIndex = candidate.length
        
        for (label in knownLabels) {
            val index = candidate.indexOf(label, ignoreCase = true)
            if (index != -1 && index < endIndex) {
                endIndex = index
            }
        }
        
        val newlineIndex = candidate.indexOf('\n')
        if (newlineIndex != -1 && newlineIndex < endIndex) {
            endIndex = newlineIndex
        }

        return candidate.substring(0, endIndex).cleanFieldValue().takeIf { it.isNotEmpty() }
    }

    data class CambioData(
        val folio: String,
        val metrosSolicitud: Double? = null,
        val metrosDiferencia: Double? = null,
        val metrosFinales: Double? = null,
        val comentarios: String = ""
    )

    fun generarMensajeValidacion(folio: Folio, ruta: Ruta?): String {
        val sb = StringBuilder()
        sb.appendLine("Validacion")
        sb.append("Folio: ").appendLine(folio.folioTruper)
        folio.nombreEstablecimiento?.let { sb.appendLine(it) }
        
        val m2Solicitud = folio.m2Reportados
        val m2Total = folio.m2Final ?: m2Solicitud

        if (m2Solicitud != null) {
            sb.append("Solicitud: ").append(formatMetros(m2Solicitud)).appendLine()
        }
        
        if (m2Total != null) {
             sb.append("Total: ").append(formatMetros(m2Total)).appendLine()
        }

        return sb.toString().trimEnd()
    }

    fun generarTextoReporteCambio(folio: Folio, m2FinalesNuevo: Double): String {
        val solicitud = folio.m2Reportados ?: 0.0
        val diferencia = m2FinalesNuevo - solicitud
        
        // Buscamos si es Mayorista (MY) o Autoservicio (MS) en observaciones o si existe un campo para ello
        val esMayorista = folio.observaciones?.contains("MY", ignoreCase = true) == true || 
                          folio.observaciones?.contains("Mayorista", ignoreCase = true) == true
        val esAutoservicio = folio.observaciones?.contains("MS", ignoreCase = true) == true ||
                          folio.observaciones?.contains("Autoservicio", ignoreCase = true) == true

        return buildString {
            appendLine("Modificación")
            append("Folio:").appendLine(folio.folioTruper)
            folio.nombreEstablecimiento?.let { appendLine(it) }
            append("Solicitud: ").append(formatMetros(solicitud)).appendLine()
            
            if (abs(diferencia) > 0.009) {
                val accion = if (diferencia > 0) "Aumenta" else "Disminuye"
                append("$accion: ").append(formatMetros(abs(diferencia))).appendLine()
            }
            
            append("Total: ").append(formatMetros(m2FinalesNuevo)).append(" M2").appendLine()
            
            if (esMayorista) {
                appendLine("la solicitud es \"my\"")
            } else if (esAutoservicio) {
                appendLine("la solicitud es \"ms\"")
            }
        }.trimEnd()
    }

    private fun String.normalizeDouble(): Double? {
        return this.replace("\\s+".toRegex(), "").replace(",", "").toDoubleOrNull()
    }

    private fun String.cleanFieldValue(): String =
        this.trim().trimStart(':', '-', '.', '·').trim().replace("\\s+".toRegex(), " ")

    private fun String.normalizeForComparison(): String {
        val sinTildes = Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace(ACENTO_REGEX, "")
        return sinTildes.lowercase().replace("\\s+".toRegex(), " ").trim()
    }

    private fun formatMetros(valor: Double): String = "${"%.2f".format(valor)} m²"

    private val ACENTO_REGEX = "\\p{Mn}+".toRegex()
}
