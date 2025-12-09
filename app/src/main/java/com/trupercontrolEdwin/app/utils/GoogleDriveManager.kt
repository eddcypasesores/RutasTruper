package com.trupercontrolEdwin.app.utils

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.client.http.FileContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleDriveManager(private val context: Context, private val account: GoogleSignInAccount) {

    private val driveService: Drive by lazy {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_FILE)
        ).setBackOff(ExponentialBackOff()).apply {
            selectedAccount = account.account
        }
        Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("ControlRotulacionesApp").build()
    }

    suspend fun uploadFile(fileToUpload: java.io.File): String? = withContext(Dispatchers.IO) {
        try {
            val folderId = getOrCreateAppFolderId()
            val fileMetadata = File().apply {
                name = fileToUpload.name
                // Busca la carpeta "RutasTruperBackup" o la crea si no existe.
                parents = listOf(folderId)
            }
            val mediaContent = FileContent("application/json", fileToUpload)

            val request = driveService.files().create(fileMetadata, mediaContent)
            request.fields = "id"
            val file = request.execute()
            
            // Eliminar respaldos antiguos para dejar solo el más reciente
            deleteOldBackups(folderId)
            
            file.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun deleteOldBackups(folderId: String) {
        try {
            // Listar archivos en la carpeta, ordenados por fecha de creación descendente (el más nuevo primero)
            val result = driveService.files().list()
                .setQ("'$folderId' in parents and trashed = false")
                .setSpaces("drive")
                .setFields("files(id)")
                .setOrderBy("createdTime desc")
                .execute()

            val files = result.files
            
            // Mantener solo el último (index 0), borrar el resto
            if (files != null && files.size > 1) {
                for (i in 1 until files.size) {
                    try {
                        driveService.files().delete(files[i].id).execute()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getOrCreateAppFolderId(): String {
        val folderName = "RutasTruperBackup"
        // Busca si la carpeta ya existe.
        val searchResult = driveService.files().list()
            .setQ("mimeType='application/vnd.google-apps.folder' and name='$folderName' and trashed=false")
            .setSpaces("drive")
            .setFields("files(id)")
            .execute()

        return if (searchResult.files.isNotEmpty()) {
            // Si la carpeta existe, retorna su ID.
            searchResult.files[0].id
        } else {
            // Si no, crea la carpeta y retorna el nuevo ID.
            val fileMetadata = File().apply {
                name = folderName
                mimeType = "application/vnd.google-apps.folder"
            }
            val file = driveService.files().create(fileMetadata).setFields("id").execute()
            file.id
        }
    }
}
