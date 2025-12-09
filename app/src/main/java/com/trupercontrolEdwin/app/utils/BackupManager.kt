package com.trupercontrolEdwin.app.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.google.gson.Gson
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Factura
import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.data.entities.Pago
import com.trupercontrolEdwin.app.data.entities.Ruta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BackupData(
    val rutas: List<Ruta>,
    val folios: List<Folio>,
    val facturas: List<Factura>,
    val pagos: List<Pago>
)

class BackupManager(private val context: Context, private val db: AppDatabase) {

    private val gson = Gson()

    suspend fun exportarDatos(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val jsonString = crearJsonBackup() ?: return@withContext false
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Crea un archivo temporal en caché (para subir a Drive y borrarlo después)
    suspend fun generarArchivoJsonLocal(): File? = withContext(Dispatchers.IO) {
        try {
            val jsonString = crearJsonBackup() ?: return@withContext null
            val fileName = "backup_temp_${System.currentTimeMillis()}.json"
            val file = File(context.cacheDir, fileName)
            FileOutputStream(file).use { it.write(jsonString.toByteArray()) }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Crea un archivo PERMANENTE en la carpeta de documentos de la app y TAMBIÉN en Descargas
    suspend fun guardarBackupLocalPermanente(): File? = withContext(Dispatchers.IO) {
        try {
            val jsonString = crearJsonBackup() ?: return@withContext null
            
            // Formato de fecha legible: Backup_Mar_2023-10-24.json
            val sdf = SimpleDateFormat("EEE_yyyy-MM-dd", Locale.getDefault())
            val fecha = sdf.format(Date())
            val fileName = "Respaldo_$fecha.json"

            // 1. Guardar en carpeta interna de la app (Android/data/.../Respaldos)
            val directorioBase = context.getExternalFilesDir(null)
            val carpetaRespaldos = File(directorioBase, "Respaldos")
            if (!carpetaRespaldos.exists()) {
                carpetaRespaldos.mkdirs()
            }
            val internalFile = File(carpetaRespaldos, fileName)
            FileOutputStream(internalFile).use { it.write(jsonString.toByteArray()) }

            // 2. Guardar en carpeta pública de Descargas (Downloads) para fácil acceso
            guardarEnDescargas(fileName, jsonString)

            // Retornamos el archivo interno para seguir usándolo en la lógica de la UI (abrir)
            // aunque el usuario probablemente buscará el de descargas manualmente.
            internalFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun guardarEnDescargas(fileName: String, content: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/RutasTruperBackup")
                }
                
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(content.toByteArray())
                    }
                }
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val appDir = File(downloadsDir, "RutasTruperBackup")
                if (!appDir.exists()) appDir.mkdirs()
                
                val file = File(appDir, fileName)
                FileOutputStream(file).use { it.write(content.toByteArray()) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun crearJsonBackup(): String? {
        return try {
            val rutas = db.rutaDao().getAllSimple()
            val folios = db.folioDao().getAllSimple()
            val facturas = db.facturaDao().getAllSimple()
            val pagos = db.pagoDao().getAllSimple()
            val backupData = BackupData(rutas, folios, facturas, pagos)
            gson.toJson(backupData)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun importarDatos(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext false
            val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
            val backupData = gson.fromJson(jsonString, BackupData::class.java)

            db.clearAllTables()
            
            backupData.rutas.forEach { db.rutaDao().insert(it) }
            backupData.folios.forEach { db.folioDao().insert(it) }
            backupData.facturas.forEach { db.facturaDao().insert(it) }
            backupData.pagos.forEach { db.pagoDao().insert(it) }
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
