package com.trupercontrolEdwin.app.utils

import com.trupercontrolEdwin.app.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FolioStatusManager(private val db: AppDatabase) {

    suspend fun actualizarEstadoFolio(folioId: Long) = withContext(Dispatchers.IO) {
        val folio = db.folioDao().getByIdSimple(folioId) ?: return@withContext
        val facturas = db.facturaDao().getByFolioSimple(folioId)

        // No hacemos nada si el folio ya está en un estado final como Cancelado o si no tiene facturas
        if (folio.estado == "Cancelado" || facturas.isEmpty()) {
            return@withContext
        }

        // 1. Calcular el total que se debe pagar por el folio
        val totalAdeudado = CalculoRotulacion.calcular(
            m2 = folio.m2Final ?: folio.m2Reportados ?: 0.0,
            tarifaTipo = folio.tarifaTipo ?: "1-100",
            figuras = folio.figuras ?: 0
        ).third

        // 2. Sumar el total de todas las facturas PAGADAS para este folio
        val totalPagado = facturas.filter { it.estado == "Pagado" }.sumOf { it.total }

        // 3. Decidir el nuevo estado
        val nuevoEstado = if (totalPagado >= totalAdeudado) {
            "Pagado"
        } else {
            "Facturado"
        }

        // 4. Actualizar el folio solo si el estado ha cambiado
        if (folio.estado != nuevoEstado) {
            db.folioDao().updateEstado(folioId, nuevoEstado)
        }
    }
}
