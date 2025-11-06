package com.trupercontrolEdwin.app.utils

import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.data.entities.Ruta

/**
 * Utilidad para interpretar los documentos (PDF o texto OCR) relacionados con los folios.
 */
object FolioParser {

    data class SolicitudData(
        val folio: String,
        val nombreNegocio: String? = null,
        val tipoFachada: String? = null,
        val direccion: String? = null,
        val metrosReportados: Double? = null
    )

    data class CambioData(
        val folio: String,
        val metrosSolicitud: Double? = null,
        val metrosDiferencia: Double? = null,
        val metrosFinales: Double? = null,
        val comentarios: String = ""
    )

    fun parseSolicitud(texto: String): SolicitudData? {
        val folio = Regex("Folio\\s*[:#-]?\\s*(\\d{4,6})", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1)
            ?: return null

        val nombre = Regex("Nombre (del negocio|de la ferreter[aí]a|de la tienda)\\s*:?\\s*(.+)", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(2)?.lineOrNull()

        val tipo = Regex("Fachada\\s*(principal|frontal|tipo)\\s*:?\\s*(.+)", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(2)?.lineOrNull()

        val direccion = Regex("(Direcci[oó]n|Domicilio)\\s*:?\\s*(.+)", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(2)?.lineOrNull()

        val metros = Regex("Total\\s*:?\\s*(\\d{1,4}(?:[.,]\\d{1,2})?)\\s*(m2|m²)", RegexOption.IGNORE_CASE)
            .find(texto)
            ?.groupValues
            ?.getOrNull(1)
            ?.replace(",", ".")
            ?.toDoubleOrNull()
            ?: Regex("\\b(\\d{1,4}(?:[.,]\\d{1,2})?)\\s*(m2|m²)\\b", RegexOption.IGNORE_CASE)
                .find(texto)
                ?.groupValues
                ?.getOrNull(1)
                ?.replace(",", ".")
                ?.toDoubleOrNull()

        return SolicitudData(
            folio = folio,
            nombreNegocio = nombre,
            tipoFachada = tipo,
            direccion = direccion,
            metrosReportados = metros
        )
    }

    fun parseCambio(texto: String): CambioData? {
        val folio = Regex("Folio\\s*[:#-]?\\s*(\\d{4,6})", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1) ?: return null

        val solicitud = Regex("(trae|solicitud).*?(\\d{1,4}(?:[.,]\\d{1,2})?)\\s*(m2|m²)", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(2)?.normalizeDouble()

        val incremento = Regex("(Incrementa|Disminuye).*?(\\d{1,4}(?:[.,]\\d{1,2})?)\\s*(m2|m²)", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(2)?.normalizeDouble()

        val total = Regex("Total.*?(\\d{1,4}(?:[.,]\\d{1,2})?)\\s*(m2|m²)", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1)?.normalizeDouble()

        val comentarios = buildString {
            if (texto.contains("mayorista", ignoreCase = true)) {
                append("Cliente mayorista. ")
            }
            val autorizo = Regex("autoriz[oó]" , RegexOption.IGNORE_CASE).find(texto)
            if (autorizo != null) {
                append("Autorizado por cliente. ")
            }
            val lineasExtras = texto.lines()
                .filter { it.contains("nota", ignoreCase = true) || it.contains("coment", ignoreCase = true) }
                .joinToString(" ")
            if (lineasExtras.isNotBlank()) append(lineasExtras.trim())
        }.trim()

        return CambioData(
            folio = folio,
            metrosSolicitud = solicitud,
            metrosDiferencia = incremento,
            metrosFinales = total,
            comentarios = comentarios
        )
    }

    fun generarMensajeValidacion(folio: Folio, ruta: Ruta?): String {
        val sb = StringBuilder()
        sb.appendLine("Validación")
        sb.append("Folio: ").appendLine(folio.folioTruper)
        ruta?.nombre?.let { sb.appendLine(it) }
        folio.nombreEstablecimiento?.let { sb.appendLine(it) }
        folio.m2Reportados?.let {
            sb.append("Solicitud: ").append(formatMetros(it)).appendLine()
        }
        folio.m2Final?.let {
            sb.append("Total de metros: ").append(formatMetros(it)).appendLine()
        }
        folio.figuras?.takeIf { it > 0 }?.let {
            sb.append("Figuras/cajones: ").append(it).appendLine()
        }
        folio.tipoFachada?.let {
            sb.append("Tipo de fachada: ").appendLine(it)
        }
        return sb.toString().trimEnd()
    }

    private fun String.lineOrNull(): String? {
        val linea = this.lines().firstOrNull()?.trim()
        return linea?.takeIf { it.isNotEmpty() }
    }

    private fun String.normalizeDouble(): Double? = replace(",", ".").toDoubleOrNull()

    private fun formatMetros(valor: Double): String = "${"%.2f".format(valor)} m²"
}
