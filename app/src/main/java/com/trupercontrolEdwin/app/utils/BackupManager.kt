package com.trupercontrolEdwin.app.utils

import android.content.Context
import com.google.gson.Gson
import com.trupercontrolEdwin.app.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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

    suspend fun exportar(rutaDestino: File): Boolean = withContext(Dispatchers.IO) {
        val rutas = db.rutaDao().getAll().firstOrNull() ?: emptyList()
        // nota: getAll() es Flow, aquí habría que crear otro DAO que regrese List<Ruta>
        val backup = BackupData(rutas, emptyList(), emptyList(), emptyList())
        rutaDestino.writeText(gson.toJson(backup))
        true
    }

    // igual se hace el importar: leer JSON → insertar en Room
}
