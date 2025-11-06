package com.trupercontrolEdwin.app.utils

import android.content.Context
import com.trupercontrolEdwin.app.data.entities.Folio
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

object ExcelGenerator {

    /**
     * Genera un Excel sencillo para el contador, con:
     * Folio, Cliente, m2, Figuras, Tarifa, Subtotal, IVA, Total
     */
    fun generarExcelParaFolios(
        context: Context,
        folios: List<Folio>,
        carpetaDestino: File
    ): File {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Facturación")

        // encabezados
        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Folio Truper")
        header.createCell(1).setCellValue("Cliente")
        header.createCell(2).setCellValue("m2")
        header.createCell(3).setCellValue("Figuras")
        header.createCell(4).setCellValue("Tarifa")
        header.createCell(5).setCellValue("Subtotal")
        header.createCell(6).setCellValue("IVA")
        header.createCell(7).setCellValue("Total")

        var rowIndex = 1
        folios.forEach { f ->
            val (subtotal, iva, total) = CalculoRotulacion.calcular(
                m2 = (f.m2Final ?: f.m2Reportados ?: 0.0),
                tarifaTipo = f.tarifaTipo ?: "1-100",
                figuras = f.figuras ?: 0
            )

            val row = sheet.createRow(rowIndex++)
            row.createCell(0).setCellValue(f.folioTruper)
            row.createCell(1).setCellValue(f.nombreEstablecimiento ?: "")
            row.createCell(2).setCellValue((f.m2Final ?: f.m2Reportados ?: 0.0))
            row.createCell(3).setCellValue((f.figuras ?: 0).toDouble())
            row.createCell(4).setCellValue(f.tarifaTipo ?: "1-100")
            row.createCell(5).setCellValue(subtotal)
            row.createCell(6).setCellValue(iva)
            row.createCell(7).setCellValue(total)
        }

        // autosize
        (0..7).forEach { sheet.autoSizeColumn(it) }

        carpetaDestino.mkdirs()
        val outFile = File(carpetaDestino, "facturacion_rotulaciones.xlsx")
        FileOutputStream(outFile).use { fos ->
            wb.write(fos)
        }
        wb.close()

        return outFile
    }
}
