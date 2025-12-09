package com.trupercontrolEdwin.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.trupercontrolEdwin.app.workers.BackupWorker
import java.util.concurrent.TimeUnit

class ControlRotulacionesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupRecurringBackup()
    }

    private fun setupRecurringBackup() {
        // Ejecutar el Worker CADA DÍA. 
        // El Worker internamente verificará si es Martes o Viernes antes de hacer el trabajo real.
        val backupWorkRequest = PeriodicWorkRequestBuilder<BackupWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "BackupWorker",
            ExistingPeriodicWorkPolicy.KEEP, 
            backupWorkRequest
        )
    }
}
