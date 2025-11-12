package com.trupercontrolEdwin.app.utils

import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.data.entities.Ruta
import java.text.Normalizer

object FolioParser {

    data class SolicitudData(
        val folio: String,
        val nombreNegocio: String? = null,
        val direccion: String? = null,
        val tipoFachada: String? = null,
        val metrosReportados: Double? = null
    )

    fun parseSolicitud(texto: String): SolicitudData? {
        val folioRegex = """(?im)Folio\s*[:#-]?\s*([0-9\s]{4,12})"""
        val folioEncontrado = Regex(folioRegex).find(texto)?.groupValues?.getOrNull(1)
        val folio = folioEncontrado?.filter { it.isDigit() }?.takeIf { it.length >= 4 } ?: return null

        val nombreNegocio = findFieldValue(
            texto,
            listOf(
                "Nombre de la ferretería a rotular",
                "Nombre de la ferreteria a rotular",
                "Nombre de la ferretería",
                "Nombre del negocio",
                "Razón social del cliente",
                "Nombre del cliente",
                "Nombre del establecimiento"
            )
        )

        val direccion = findFieldValue(
            texto,
            listOf(
                "Domicilio de la ferretería a rotular",
                "Domicilio de la ferreteria a rotular",
                "Dirección del establecimiento",
                "Dirección de la ferretería",
                "Dirección"
            )
        )

        val tipoFachada = findFieldValue(
            texto,
            listOf(
                "Material de fachada",
                "Material de fachada actual",
                "Tipo de fachada",
                "Tipo de rotulación"
            )
        )

        val metros = findFieldValue(
            texto,
            listOf(
                "Total de metros cuadrados a rotular",
                "Metros con valor sin promoción",
                "Total metros rotulados",
                "Total metros solicitud",
                "Metros rotulados",
                "Metros totales",
                "Total m2"
            )
        )?.normalizeDouble()

        return SolicitudData(
            folio = folio,
            nombreNegocio = nombreNegocio,
            direccion = direccion,
            tipoFachada = tipoFachada,
            metrosReportados = metros
        )
    }

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

    private fun findFieldValue(texto: String, etiquetas: List<String>): String? {
        if (etiquetas.isEmpty()) return null
        val normalizedEtiquetas = etiquetas.map { it.normalizeForComparison() }
        val normalizedEtiquetasNoSpaces = normalizedEtiquetas.map { it.removeSpaces() }
        val lineas = texto.lines()
        for (index in lineas.indices) {
            val lineaOriginal = lineas[index].trim()
            if (lineaOriginal.isEmpty()) continue
            val lineaNormalizada = lineaOriginal.normalizeForComparison()
            val lineaSinEspacios = lineaNormalizada.removeSpaces()
            val coincide = normalizedEtiquetas.indexOfFirst { etiqueta ->
                lineaNormalizada.contains(etiqueta) ||
                        lineaSinEspacios.contains(normalizedEtiquetasNoSpaces[it])
            }
            if (coincide == -1) continue

            val valorDirecto = lineaOriginal.substringAfter(':', "").takeIf { it.isNotEmpty() }
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
                ?: lineaOriginal.substringAfter('-', "")
                    .trim()
                    .takeIf { it.isNotEmpty() && it.length < lineaOriginal.length }

            val valorLimpio = valorDirecto?.cleanFieldValue()
            if (!valorLimpio.isNullOrEmpty()) return valorLimpio

            for (offset in 1..2) {
                val siguiente = lineas.getOrNull(index + offset)?.trim().orEmpty()
                if (siguiente.isEmpty()) continue
                val siguienteNormalizado = siguiente.normalizeForComparison()
                val siguienteSinEspacios = siguienteNormalizado.removeSpaces()
                val esOtraEtiqueta = normalizedEtiquetas.indices.any { etiquetaIndex ->
                    val etiqueta = normalizedEtiquetas[etiquetaIndex]
                    val etiquetaSinEspacios = normalizedEtiquetasNoSpaces[etiquetaIndex]
                    siguienteNormalizado.startsWith(etiqueta) ||
                            siguienteSinEspacios.startsWith(etiquetaSinEspacios)
                }
                if (esOtraEtiqueta) break
                if (siguiente.contains(':')) break
                val valorSiguiente = siguiente.cleanFieldValue()
                if (valorSiguiente.isNotEmpty()) return valorSiguiente
            }
        }
        return null
    }

    private fun String.normalizeDouble(): Double? {
        val compact = trim().replace("\\s+".toRegex(), "")
        if (compact.isEmpty()) return null

        val decimalIndex = compact.indexOfLast { it == ',' || it == '.' }
        val builder = StringBuilder()
        compact.forEachIndexed { index, c ->
            when {
                c.isDigit() -> builder.append(c)
                index == decimalIndex && c in charArrayOf(',', '.') -> builder.append('.')
            }
        }

        return builder.toString().toDoubleOrNull()
    }

    private fun String.cleanFieldValue(): String =
        this.trim().trimStart(':', '-', '.', '·').trim().replace("\\s+".toRegex(), " ")

    private fun String.normalizeForComparison(): String {
        val sinTildes = Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace(ACENTO_REGEX, "")
        return sinTildes.lowercase().replace("\\s+".toRegex(), " ").trim()
    }

    private fun formatMetros(valor: Double): String = "${"%.2f".format(valor)} m²"

    private fun String.removeSpaces(): String = replace("\\s+".toRegex(), "")

    private val ACENTO_REGEX = "\\p{Mn}+".toRegex()
}
