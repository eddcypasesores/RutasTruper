package com.trupercontrolEdwin.app.utils

import com.trupercontrolEdwin.app.data.entities.Folio
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

object ExcelGenerator {

    /**
     * Genera un Excel con los folios proporcionados.
     * Columnas: Nombre del Establecimiento, Direccion, M2, No.Figuras, Precio unitario (Subtotal), Iva, Total
     * Nota: No cierra el outputStream, eso es responsabilidad del llamador.
     */
    fun generarExcelParaFolios(
        folios: List<Folio>,
        outputStream: OutputStream
    ) {
        val wb = XSSFWorkbook()
        try {
            val sheet = wb.createSheet("Facturación")

            // Encabezados
            val header = sheet.createRow(0)
            val headers = listOf(
                "Nombre del Establecimiento", 
                "Direccion", 
                "M2", 
                "No.Figuras", 
                "Precio unitario", // Subtotal
                "Iva", 
                "Total"
            )
            
            headers.forEachIndexed { index, title ->
                header.createCell(index).setCellValue(title)
            }

            var rowIndex = 1
            folios.forEach { f ->
                // Calcular montos
                val m2 = f.m2Final ?: f.m2Reportados ?: 0.0
                val figuras = f.figuras ?: 0
                val tarifa = f.tarifaTipo ?: "1-100"
                val (subtotal, iva, total) = CalculoRotulacion.calcular(m2, tarifa, figuras)

                val row = sheet.createRow(rowIndex++)
                
                // 1. Nombre del Establecimiento
                row.createCell(0).setCellValue(f.nombreEstablecimiento ?: "")
                
                // 2. Direccion
                row.createCell(1).setCellValue(f.direccion ?: "")
                
                // 3. M2 (m2 finales)
                row.createCell(2).setCellValue(m2)
                
                // 4. No.Figuras (Figuras)
                row.createCell(3).setCellValue(figuras.toDouble())
                
                // 5. Precio unitario (subtotal)
                row.createCell(4).setCellValue(subtotal)
                
                // 6. Iva
                row.createCell(5).setCellValue(iva)
                
                // 7. Total
                row.createCell(6).setCellValue(total)
            }

            // Ajustar tamaño de columnas manualmente en lugar de autoSizeColumn (que requiere java.awt)
            // El ancho se mide en unidades de 1/256 de un carácter. 
            // Por ejemplo, 256 * 20 es aproximadamente 20 caracteres de ancho.
            sheet.setColumnWidth(0, 256 * 30) // Nombre
            sheet.setColumnWidth(1, 256 * 40) // Dirección
            sheet.setColumnWidth(2, 256 * 15) // M2
            sheet.setColumnWidth(3, 256 * 15) // Figuras
            sheet.setColumnWidth(4, 256 * 15) // P. Unitario
            sheet.setColumnWidth(5, 256 * 15) // IVA
            sheet.setColumnWidth(6, 256 * 15) // Total

            // Escribimos al stream proporcionado SIN cerrarlo aquí
            wb.write(outputStream)
        } finally {
            // Solo cerramos el workbook para liberar memoria de POI
            wb.close()
        }
    }
}
