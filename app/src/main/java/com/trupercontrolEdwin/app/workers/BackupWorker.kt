package com.trupercontrolEdwin.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.utils.BackupManager
import com.trupercontrolEdwin.app.utils.GoogleDriveManager
import java.util.Calendar

class BackupWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val context = applicationContext

        // Verificar si hoy es Martes (3) o Viernes (6)
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Si NO es martes NI viernes, terminamos exitosamente sin hacer nada
        if (dayOfWeek != Calendar.TUESDAY && dayOfWeek != Calendar.FRIDAY) {
            return Result.success()
        }

        val db = AppDatabase.get(context)
        val backupManager = BackupManager(context, db)

        // 1. Generar respaldo LOCAL permanente (siempre se intenta)
        val localFile = backupManager.guardarBackupLocalPermanente()
        if (localFile == null) {
            // Si falla el local, reintentamos, ya que es crítico
            return Result.retry() 
        }

        // 2. Subir a Google Drive (solo si hay sesión iniciada)
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            try {
                // Para subir a Drive usamos el archivo temporal o el permanente.
                // Usaremos el permanente que acabamos de crear.
                val driveManager = GoogleDriveManager(context, account)
                val fileId = driveManager.uploadFile(localFile)
                
                if (fileId == null) {
                    // Falló la subida a Drive. 
                    // Podríamos retornar retry() si queremos insistir en la subida a la nube,
                    // pero como ya tenemos el respaldo local, podemos considerar el trabajo como parcialmente exitoso
                    // o reintentar solo si es crítico. 
                    // Para este caso, vamos a reintentar para asegurar la nube también.
                    return Result.retry()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return Result.retry()
            }
        }

        return Result.success()
    }
}
