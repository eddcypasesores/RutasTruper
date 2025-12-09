package com.trupercontrolEdwin.app.utils

import com.trupercontrolEdwin.app.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class ReportesManager(private val db: AppDatabase) {

    /**
     * Calcula la suma de subtotal e IVA para un mes y año específicos.
     */
    suspend fun getTotalesPorMes(mes: Int, anio: Int): Pair<Double, Double> = withContext(Dispatchers.IO) {
        val cal = Calendar.getInstance()
        cal.set(anio, mes, 1, 0, 0, 0)
        val inicio = cal.timeInMillis

        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.MILLISECOND, -1)
        val fin = cal.timeInMillis

        val subtotal = db.facturaDao().getSumSubtotalInPeriod(inicio, fin) ?: 0.0
        val iva = db.facturaDao().getSumIvaInPeriod(inicio, fin) ?: 0.0

        Pair(subtotal, iva)
    }

    /**
     * Calcula la suma de subtotal e IVA para un rango de fechas (en milisegundos).
     */
    suspend fun getTotalesPorRango(inicio: Long, fin: Long): Pair<Double, Double> = withContext(Dispatchers.IO) {
        // Aseguramos que 'fin' sea el final del día.
        val cal = Calendar.getInstance()
        cal.timeInMillis = fin
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val finDelDia = cal.timeInMillis

        val subtotal = db.facturaDao().getSumSubtotalInPeriod(inicio, finDelDia) ?: 0.0
        val iva = db.facturaDao().getSumIvaInPeriod(inicio, finDelDia) ?: 0.0
        Pair(subtotal, iva)
    }
}
