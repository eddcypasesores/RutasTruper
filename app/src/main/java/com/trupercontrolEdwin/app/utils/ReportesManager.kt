package com.trupercontrolEdwin.app.utils

import com.trupercontrolEdwin.app.data.dao.FacturaDao
import com.trupercontrolEdwin.app.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Reportes de:
 * - total facturado por mes
 * - total pagado
 * - pendiente
 */
class ReportesManager(private val db: AppDatabase) {

    // Esto se puede mejorar con queries directas en el DAO
    suspend fun obtenerTotalesGenerales(): Triple<Double, Double, Double> = withContext(Dispatchers.IO) {
        val facturas = db.facturaDao().getPendientes() // flow, pero aquí es una demo
        // en producción: hacer queries SUM() en DAO
        Triple(0.0, 0.0, 0.0)
    }
}
