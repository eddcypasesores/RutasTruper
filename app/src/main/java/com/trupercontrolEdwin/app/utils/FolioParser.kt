package com.trupercontrolEdwin.app.utils

import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.data.entities.Ruta

object FolioParser {

    data class SolicitudData(
        val folio: String,
        val nombreNegocio: String? = null,
        val direccion: String? = null,
        val metrosReportados: Double? = null
    )

    fun parseSolicitud(texto: String): SolicitudData? {
        val folio = Regex("""Folio:\s*(\d{5,6})""", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1)
            ?: return null

        val razonSocial = Regex("""Razón social del cliente:\s*(.*?)\n""", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1)?.trim()

        val direccion = Regex("""Domicilio de la ferretería a rotular:\s*(.*?)\n""", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1)?.trim()

        val metros = Regex("""Total de metros cuadrados a rotular:\s*(\d+\.\d{2})""", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1)?.toDoubleOrNull()

        return SolicitudData(
            folio = folio,
            nombreNegocio = razonSocial,
            direccion = direccion,
            metrosReportados = metros
        )
    }

    // ... (El resto del código se mantiene igual)

    data class CambioData(
        val folio: String,
        val metrosSolicitud: Double? = null,
        val metrosDiferencia: Double? = null,
        val metrosFinales: Double? = null,
        val comentarios: String = ""
    )

    data class FacturaData(
        val folioFactura: String,
        val total: Double
    )

    fun parsearDocumentoFactura(texto: String): FacturaData? {
        val folio = Regex("(Folio Fiscal|UUID).+?([a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.get(2)?.uppercase()

        val total = Regex("""Total\s+\$([\d,]+\.\d{2})""")
            .find(texto)?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull()

        if (folio == null || total == null) return null
        return FacturaData(folio, total)
    }

    fun parseCambio(texto: String): CambioData? {
        val folio = Regex("""Folio\s*[:#-]?\s*(\d{4,6})""", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1) ?: return null

        val solicitud = Regex("""(trae|solicitud).*?(\d{1,4}(?:[.,]\d{1,2})?)\s*(m2|m²)""", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(2)?.normalizeDouble()

        val incremento = Regex("""(Incrementa|Disminuye).*?(\d{1,4}(?:[.,]\d{1,2})?)\s*(m2|m²)""", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(2)?.normalizeDouble()

        val total = Regex("""Total.*?(\d{1,4}(?:[.,]\d{1,2})?)\s*(m2|m²)""", RegexOption.IGNORE_CASE)
            .find(texto)?.groupValues?.getOrNull(1)?.normalizeDouble()

        val comentarios = buildString {
            if (texto.contains("mayorista", ignoreCase = true)) {
                append("Cliente mayorista. ")
            }
            val autorizo = Regex("""autoriz[oó]""" , RegexOption.IGNORE_CASE).find(texto)
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