package com.trupercontrolEdwin.app.utils

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.trupercontrolEdwin.app.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.OutputStream

data class BackupData(
    val rutas: List<com.trupercontrolEdwin.app.data.entities.Ruta>,
    val folios: List<com.trupercontrolEdwin.app.data.entities.Folio>,
    val facturas: List<com.trupercontrolEdwin.app.data.entities.Factura>,
    val pagos: List<com.trupercontrolEdwin.app.data.entities.Pago>
)

class BackupManager(
    private val context: Context,
    private val db: AppDatabase
) {

    private val gson = Gson()

    suspend fun exportar(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val rutas = db.rutaDao().getAll().first()
            val folios = db.folioDao().getAllSimple()
            val facturas = db.facturaDao().getAllSimple()
            val pagos = db.pagoDao().getAllSimple()

            val backup = BackupData(rutas, folios, facturas, pagos)
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(gson.toJson(backup).toByteArray())
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    // igual se hace el importar: leer JSON → insertar en Room
}
