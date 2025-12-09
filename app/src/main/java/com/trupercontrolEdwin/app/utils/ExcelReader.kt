package com.trupercontrolEdwin.app.utils

import android.content.Context
import android.net.Uri
import com.trupercontrolEdwin.app.utils.FolioParser.SolicitudData
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object ExcelReader {

    fun leerExcelSolicitudes(context: Context, uri: Uri): List<SolicitudData> {
        val solicitudes = mutableListOf<SolicitudData>()

        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val workbook = XSSFWorkbook(inputStream)
                val sheet = workbook.getSheetAt(0)

                if (sheet.physicalNumberOfRows == 0) return emptyList()

                // 1. Mapear cabeceras
                val headerRow = sheet.getRow(0)
                val colMap = mutableMapOf<String, Int>()

                for (cell in headerRow) {
                    val colName = getCellValueAsString(cell).trim().lowercase()
                    colMap[colName] = cell.columnIndex
                }

                // Columnas requeridas
                val idxFolio = findColIndex(colMap, listOf("folio", "no. folio"))
                val idxNombre = findColIndex(colMap, listOf("nombre de la ferretería", "cliente", "nombre"))
                val idxM2 = findColIndex(colMap, listOf("metros cuadrados a rotular (total)", "m2", "total metros"))
                val idxDireccion = findColIndex(colMap, listOf("domicilio de la ferretería", "domicilio", "dirección"))
                
                // Nuevas columnas solicitadas
                val idxFachada = findColIndex(colMap, listOf("tipo de fachada", "fachada"))
                val idxRotulacion = findColIndex(colMap, listOf("tipo de rotulacion", "tipo de rotulación", "tipo solicitud"))

                if (idxFolio == -1) return emptyList()

                // 2. Leer filas
                for (i in 1..sheet.lastRowNum) {
                    val row = sheet.getRow(i) ?: continue
                    
                    val rawFolio = getCellValueAsString(row.getCell(idxFolio))
                    val folio = rawFolio.filter { it.isDigit() }

                    if (folio.length < 4) continue 

                    val nombre = if (idxNombre != -1) getCellValueAsString(row.getCell(idxNombre)) else null
                    val direccion = if (idxDireccion != -1) getCellValueAsString(row.getCell(idxDireccion)) else null
                    
                    // Metros
                    val metrosStr = if (idxM2 != -1) getCellValueAsString(row.getCell(idxM2)) else ""
                    val metrosClean = metrosStr.lowercase().replace("m2", "").replace(",", "").trim()
                    val metros = metrosClean.toDoubleOrNull()

                    // Tipo de Fachada (Opcion A, B, etc)
                    var fachada = if (idxFachada != -1) getCellValueAsString(row.getCell(idxFachada)) else null
                    
                    // Si el excel no trae nada en fachada, definimos "Sin Opción"
                    if (fachada.isNullOrBlank()) {
                        fachada = "Sin Opción"
                    }

                    // Tipo de Rotulación (MY, MS, etc)
                    val rotulacion = if (idxRotulacion != -1) getCellValueAsString(row.getCell(idxRotulacion)) else ""

                    // Retornamos el objeto
                    solicitudes.add(
                        SolicitudData(
                            folio = folio,
                            nombreNegocio = nombre?.ifBlank { null },
                            direccion = direccion?.ifBlank { null },
                            tipoFachada = fachada,
                            metrosReportados = metros,
                            // Pasamos el tipo de rotulación (MY/MS) en extraInfo.
                            // Esto será crucial para detectar MS/MY
                            extraInfo = rotulacion.takeIf { it.isNotBlank() }
                        )
                    )
                }
                workbook.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return solicitudes
    }

    private fun findColIndex(map: Map<String, Int>, keys: List<String>): Int {
        for (key in keys) {
            val k = key.lowercase()
            if (map.containsKey(k)) return map[k]!!
            map.keys.find { it == k }?.let { return map[it]!! }
        }
        return -1
    }

    private fun getCellValueAsString(cell: Cell?): String {
        if (cell == null) return ""
        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    cell.dateCellValue.toString()
                } else {
                    val value = cell.numericCellValue
                    if (value == value.toLong().toDouble()) {
                        value.toLong().toString()
                    } else {
                        value.toString()
                    }
                }
            }
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            else -> ""
        }
    }
}
